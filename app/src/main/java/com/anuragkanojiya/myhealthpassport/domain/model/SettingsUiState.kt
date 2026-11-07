package com.anuragkanojiya.myhealthpassport.domain.model

data class SettingsUiState(
    val userEmail: String = "",
    val userName: String = "", // Hoisted from Repository
    val isDarkMode: Boolean = false,
    val hasApiKey: Boolean = false,
    val appVersion: String = "1.0",
    val isNameDialogOpen: Boolean = false, // Control dialog visibility
    val tempUserName: String = "", // For text field input before saving
    val selectedLanguageTag: String = "", // Language tag e.g. "en", "es", "hi" (empty = system default)
    val isLanguageDialogOpen: Boolean = false
)