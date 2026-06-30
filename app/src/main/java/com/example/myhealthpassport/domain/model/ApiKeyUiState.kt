package com.example.myhealthpassport.domain.model

data class ApiKeyUiState(
    val apiKey: String = "",
    val isSaved: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed class ApiKeyUiEvent {
    data class ApiKeyChanged(val value: String) : ApiKeyUiEvent()
    object SaveClicked : ApiKeyUiEvent()
    object ClearError : ApiKeyUiEvent()
}