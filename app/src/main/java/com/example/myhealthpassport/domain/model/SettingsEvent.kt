package com.example.myhealthpassport.domain.model

sealed class SettingsEvent {
    data class ToggleDarkMode(val enabled: Boolean) : SettingsEvent()
    object SignOut : SettingsEvent()
    object ClearApiKey : SettingsEvent()
    object OpenApiKeySettings : SettingsEvent()

    data class ShowNameDialog(val show: Boolean) : SettingsEvent()
    data class UpdateTempName(val name: String) : SettingsEvent()
    object SaveUserName : SettingsEvent()
    object RequestMedicalIdView : SettingsEvent()
}