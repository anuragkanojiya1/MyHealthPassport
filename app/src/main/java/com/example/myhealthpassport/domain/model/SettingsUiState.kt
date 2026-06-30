package com.example.myhealthpassport.domain.model

data class SettingsUiState(
    val userEmail: String = "",
    val userName: String = "", // Hoisted from Repository
    val isDarkMode: Boolean = false,
    val hasApiKey: Boolean = false,
    val appVersion: String = "1.0.0",
    val isNameDialogOpen: Boolean = false, // Control dialog visibility
    val tempUserName: String = "" // For text field input before saving
)