package com.example.myhealthpassport.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myhealthpassport.ui.composables.UiState
import com.example.myhealthpassport.domain.model.UserHealthData
import com.example.myhealthpassport.data.datastore.GetApiKeyUseCase
import com.example.myhealthpassport.domain.repository.HealthRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AiViewModel @Inject constructor(
    private val getApiKeyUseCase: GetApiKeyUseCase,
    private val healthRepository: HealthRepository
) : ViewModel() {

    private val tag = "AiViewModel"

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _medicalIds = MutableStateFlow<List<String>>(emptyList())
    val medicalIds: StateFlow<List<String>> = _medicalIds.asStateFlow()

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

    private suspend fun getGenerativeModel(): GenerativeModel {
        val apiKey = getApiKeyUseCase().first() ?: ""
        return GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = apiKey
        )
    }

    private fun cleanJsonResponse(response: String): String {
        val trimmed = response.trim()
        val startIndex = trimmed.indexOf('{')
        val endIndex = trimmed.lastIndexOf('}')
        return if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            trimmed.substring(startIndex, endIndex + 1)
        } else {
            trimmed
        }
    }

    private fun parseSafeInt(jsonObject: JSONObject, key: String, defaultValue: Int): Int {
        val value = jsonObject.opt(key) ?: return defaultValue
        if (value is Int) return value
        if (value is String) {
            val numericPart = value.filter { it.isDigit() || it == '-' }
            return numericPart.toIntOrNull() ?: defaultValue
        }
        return defaultValue
    }

    private fun parseSafeFloat(jsonObject: JSONObject, key: String, defaultValue: Float): Float {
        val value = jsonObject.opt(key) ?: return defaultValue
        if (value is Double) return value.toFloat()
        if (value is Int) return value.toFloat()
        if (value is String) {
            val numericPart = value.filter { it.isDigit() || it == '.' || it == '-' }
            return numericPart.toFloatOrNull() ?: defaultValue
        }
        return defaultValue
    }

    fun extractMedicalReport(jsonResponse: String): UserHealthData {
        val cleanJson = cleanJsonResponse(jsonResponse)
        val jsonObject = JSONObject(cleanJson)
        return UserHealthData(
            name = jsonObject.optString("name", "Unknown"),
            bloodGroup = jsonObject.optString("bloodGroup", "Not Provided"),
            age = parseSafeInt(jsonObject, "age", 0),
            systolicBP = parseSafeInt(jsonObject, "systolicBP", 130),
            diastolicBP = parseSafeInt(jsonObject, "diastolicBP", 85),
            bloodSugarLevel = parseSafeInt(jsonObject, "bloodSugarLevel", 120),
            gender = jsonObject.optString("gender", "Not Provided"),
            healthCondition = jsonObject.optString("healthCondition", "No Condition Recorded"),
            emergencyPhoneNumber = jsonObject.optString("emergencyPhoneNumber", "0000000000"),
            address = jsonObject.optString("address", "No Address Provided"),
            allergies = jsonObject.optString("allergies", "None"),
            medications = jsonObject.optString("medications", "No Medications"),
            weight = parseSafeFloat(jsonObject, "weight", 0.0f),
            height = parseSafeFloat(jsonObject, "height", 0.0f)
        )
    }

    fun analyzeData(prompt: String, onResult: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val model = getGenerativeModel()
                val response = model.generateContent(
                    content { text(prompt) }
                )
                response.text?.let { outputContent ->
                    onResult(outputContent)
                }
            } catch (e: Exception) {
                Log.d("$tag Error: ", e.localizedMessage ?: "Unknown error")
                onResult("$tag Error: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }

    fun sendPrompt(bitmap: Bitmap, prompt: String, choice: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val model = getGenerativeModel()
                val response = model.generateContent(
                    content {
                        image(bitmap)
                        text(prompt)
                    }
                )


                val outputText = response.text ?: ""

                if (choice == "Save") {
                    try {
                        val userHealthData = extractMedicalReport(outputText)
                        _uiState.value = UiState.ExtractedData(userHealthData)
                    } catch (e: Exception) {
                        Log.d("$tag Error: ", e.localizedMessage ?: "Parsing error")
                        _uiState.value = UiState.Error("AI response was not in a valid format to save.")
                    }
                } else {
                    _uiState.value = UiState.Success(outputText)
                }
            } catch (e: Exception) {
                Log.d("$tag Error: ", e.localizedMessage ?: "General error")
                _uiState.value = UiState.Error(e.localizedMessage ?: "Analysis failed")
            }
        }
    }

    fun saveExtractedData(data: UserHealthData) {
        _uiState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = healthRepository.saveHealthData(data)
            result.onSuccess {
                _uiState.value = UiState.Success("Report saved successfully for: ${data.name} (ID: ${data.medicalID})")
            }.onFailure { e ->
                Log.d("$tag Error: ", e.localizedMessage ?: "Save failed")
                _uiState.value = UiState.Error("Failed to save: ${e.localizedMessage}")
            }
        }
    }

    fun resetState() {
        _uiState.value = UiState.Initial
    }
}
