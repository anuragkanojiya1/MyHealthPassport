package com.anuragkanojiya.myhealthpassport.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.anuragkanojiya.myhealthpassport.data.worker.GeminiWorker
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import com.anuragkanojiya.myhealthpassport.ui.composables.UiState
import com.anuragkanojiya.myhealthpassport.di.IoDispatcher
import com.anuragkanojiya.myhealthpassport.domain.model.UserHealthData
import com.anuragkanojiya.myhealthpassport.domain.repository.HealthRepository
import com.anuragkanojiya.myhealthpassport.domain.usecase.GeminiAnalysisUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiViewModel @Inject constructor(
    private val geminiAnalysisUseCase: GeminiAnalysisUseCase,
    private val healthRepository: HealthRepository,
    private val workManager: WorkManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val tag = "AiViewModel"

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _medicalIds = MutableStateFlow<List<String>>(emptyList())
    val medicalIds: StateFlow<List<String>> = _medicalIds.asStateFlow()

    // State for background handoff
    private var currentBitmap: Bitmap? = null
    private var currentPrompt: String? = null
    private var currentMedicalId: String? = null
    private var isBackgroundHandoffTriggered = false

    init {
        fetchMedicalIds()
    }

    private fun fetchMedicalIds() {
        viewModelScope.launch {
            healthRepository.fetchMedicalIDs().onSuccess {
                _medicalIds.value = it
            }
        }
    }

    fun analyzeData(prompt: String, onResult: (String) -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            geminiAnalysisUseCase.analyzeData(prompt)
                .onSuccess { onResult(it) }
                .onFailure {
                    Log.d("$tag Error: ", it.localizedMessage ?: "Unknown error")
                    onResult("$tag Error: ${it.localizedMessage ?: "Unknown error"}")
                }
        }
    }

    fun sendPrompt(bitmap: Bitmap, prompt: String, choice: String) {
        currentBitmap = bitmap
        currentPrompt = prompt
        isBackgroundHandoffTriggered = false

        _uiState.value = UiState.Loading
        viewModelScope.launch(ioDispatcher) {
            geminiAnalysisUseCase.analyzeImage(bitmap, prompt)
                .onSuccess { outputText ->
                    if (choice == "Save") {
                        try {
                            val userHealthData = geminiAnalysisUseCase.extractMedicalReport(outputText)
                            _uiState.value = UiState.ExtractedData(userHealthData)
                        } catch (e: Exception) {
                            Log.d("$tag Error: ", e.localizedMessage ?: "Parsing error")
                            _uiState.value = UiState.Error("AI response was not in a valid format to save.")
                        }
                    } else {
                        _uiState.value = UiState.Success(outputText)
                    }
                    // Clear pending task on success to avoid background trigger
                    currentBitmap = null
                    currentPrompt = null
                }
                .onFailure {
                    Log.d("$tag Error: ", it.localizedMessage ?: "General error")
                    _uiState.value = UiState.Error(it.localizedMessage ?: "Analysis failed")
                }
        }
    }

    fun saveExtractedData(data: UserHealthData) {
        _uiState.value = UiState.Loading
        viewModelScope.launch(ioDispatcher) {
            val result = healthRepository.saveHealthData(data)
            result.onSuccess {
                _uiState.value = UiState.Success("Report saved successfully for: ${data.name} (ID: ${data.medicalID})")
            }.onFailure { e ->
                Log.d("$tag Error: ", e.localizedMessage ?: "Save failed")
                _uiState.value = UiState.Error("Failed to save: ${e.localizedMessage}")
            }
        }
    }

    fun setAutoHandoffData(bitmap: Bitmap?, medicalId: String?) {
        currentBitmap = bitmap
        currentMedicalId = medicalId
    }

    fun triggerBackgroundHandoff(cacheDir: File, defaultPrompt: String) {
        if (isBackgroundHandoffTriggered) return
        
        val bitmap = currentBitmap
        val medicalId = currentMedicalId ?: medicalIds.value.firstOrNull() ?: "Unknown"
        val prompt = currentPrompt ?: defaultPrompt

        // Trigger if currently loading OR if bitmap is selected but not processed
        if (bitmap != null && (_uiState.value is UiState.Loading || currentPrompt != null)) {
            isBackgroundHandoffTriggered = true
            startBackgroundAnalysis(medicalId, prompt, bitmap, cacheDir)
            Log.d(tag, "Background handoff automatically triggered for Medical ID: $medicalId")
        }
    }

    fun resetState() {
        _uiState.value = UiState.Initial
        isBackgroundHandoffTriggered = false
    }

    fun startBackgroundAnalysis(medicalID: String, prompt: String, bitmap: Bitmap? = null, cacheDir: File? = null) {
        _uiState.value = UiState.Loading
        
        var imagePath: String? = null
        if (bitmap != null && cacheDir != null) {
            try {
                val file = File(cacheDir, "temp_ai_image_${UUID.randomUUID()}.jpg")
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                }
                imagePath = file.absolutePath
            } catch (e: Exception) {
                Log.e(tag, "Failed to save temp image", e)
            }
        }

        val workRequest = OneTimeWorkRequestBuilder<GeminiWorker>()
            .setInputData(workDataOf(
                "prompt" to prompt,
                "medicalID" to medicalID,
                "imagePath" to imagePath
            ))
            .build()
        
        workManager.enqueueUniqueWork(
            "ai_analysis_handoff",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
        _uiState.value = UiState.BackgroundScheduled
    }
}
