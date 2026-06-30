package com.example.myhealthpassport.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myhealthpassport.data.datastore.GetApiKeyUseCase
import com.example.myhealthpassport.data.datastore.SaveApiKeyUseCase
import com.example.myhealthpassport.domain.model.ApiKeyUiEvent
import com.example.myhealthpassport.domain.model.ApiKeyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiKeyViewModel @Inject constructor(
    private val saveApiKeyUseCase: SaveApiKeyUseCase,
    private val getApiKeyUseCase: GetApiKeyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApiKeyUiState())
    val uiState = _uiState.asStateFlow()

    // Observable property to check if onboarding is needed
    val isApiKeyMissing: StateFlow<Boolean?> = getApiKeyUseCase()
        .map { it.isNullOrBlank() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        loadExistingKey()
    }

    private fun loadExistingKey() {
        viewModelScope.launch {
            getApiKeyUseCase().collect { key ->
                _uiState.update { it.copy(apiKey = key ?: "") }
            }
        }
    }

    fun onEvent(event: ApiKeyUiEvent) {
        when (event) {
            is ApiKeyUiEvent.ApiKeyChanged -> {
                _uiState.update { it.copy(apiKey = event.value, isSaved = false) }
            }
            is ApiKeyUiEvent.SaveClicked -> saveKey()
            is ApiKeyUiEvent.ClearError -> _uiState.update { it.copy(errorMessage = null) }
        }
    }

    private fun saveKey() {
        val key = _uiState.value.apiKey
        if (key.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Key cannot be empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            saveApiKeyUseCase(key)
            _uiState.update { it.copy(isLoading = false, isSaved = true) }
        }
    }
}