package com.example.myhealthpassport.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myhealthpassport.BuildConfig
import com.example.myhealthpassport.data.datastore.UserPreferencesRepository
import com.example.myhealthpassport.domain.model.SettingsEvent
import com.example.myhealthpassport.domain.model.SettingsUiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: UserPreferencesRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = combine(
        repository.isDarkModeFlow,
        repository.apiKeyFlow,
        _uiState
    ) { isDarkMode, apiKey, localUi ->
        // Merge repository-driven values with UI-only state (dialog, tempName, etc.)
        localUi.copy(
            userEmail = auth.currentUser?.email ?: "No Email",
            isDarkMode = isDarkMode,
            hasApiKey = !apiKey.isNullOrBlank(),
            appVersion = "${BuildConfig.VERSION_NAME} (${BuildConfig.BUILD_TYPE})"
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ToggleDarkMode -> {
                viewModelScope.launch { repository.setDarkMode(event.enabled) }
            }

            is SettingsEvent.SignOut -> auth.signOut()
            is SettingsEvent.ClearApiKey -> {
                viewModelScope.launch { repository.clearApiKey() }
            }
            is SettingsEvent.ShowNameDialog -> {
                _uiState.update { it.copy(
                    isNameDialogOpen = event.show,
                    // Initialize temp name with current name when opening
                    tempUserName = if (event.show) it.userName else ""
                ) }
            }
            is SettingsEvent.UpdateTempName -> {
                _uiState.update { it.copy(tempUserName = event.name) }
            }
            is SettingsEvent.SaveUserName -> {
                val newName = _uiState.value.tempUserName
                viewModelScope.launch {
                    repository.saveUserName(newName)
                    _uiState.update {
                        it.copy(
                            userName = newName,
                            isNameDialogOpen = false
                        )
                    }
                }
            }

            else -> {}
        }
    }

    fun saveUserName(userName: String){
        viewModelScope.launch {
            repository.saveUserName(userName)
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            repository.setDarkMode(enabled)
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun clearApiKey() {
        viewModelScope.launch {
            repository.clearApiKey()
        }
    }
}
