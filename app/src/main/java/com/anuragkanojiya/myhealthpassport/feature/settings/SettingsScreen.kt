package com.anuragkanojiya.myhealthpassport.feature.settings

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.res.stringResource
import com.anuragkanojiya.myhealthpassport.R
import com.anuragkanojiya.myhealthpassport.core.security.BiometricPromptManager
import com.anuragkanojiya.myhealthpassport.domain.model.SettingsEvent
import com.anuragkanojiya.myhealthpassport.navigation.Screen
import com.anuragkanojiya.myhealthpassport.core.designsystem.theme.HealthBlue
import com.anuragkanojiya.myhealthpassport.core.designsystem.theme.HealthBlueDark
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.shadow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel(),
    promptManager: BiometricPromptManager? = null
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(promptManager) {
        promptManager?.promptResults?.collect { result ->
            when (result) {
                is BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                    navController.navigate("patient_details/all_records")
                }
                is BiometricPromptManager.BiometricResult.AuthenticationError -> {
                    Toast.makeText(context, "Auth Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is BiometricPromptManager.BiometricResult.AuthenticationFailed -> {
                    Toast.makeText(context, "Authentication Failed", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    val languages = listOf(
        "" to stringResource(R.string.settings_language_system_default),
        "en" to stringResource(R.string.lang_english),
        "es" to stringResource(R.string.lang_spanish),
        "hi" to stringResource(R.string.lang_hindi),
        "fr" to stringResource(R.string.lang_french),
        "de" to stringResource(R.string.lang_german),
        "ja" to stringResource(R.string.lang_japanese),
        "pt" to stringResource(R.string.lang_portuguese),
        "zh" to stringResource(R.string.lang_chinese),
        "ar" to stringResource(R.string.lang_arabic)
    )
    val currentLangName = languages.firstOrNull { it.first == state.selectedLanguageTag }?.second
        ?: stringResource(R.string.settings_language_system_default)

    // Handle Name Edit Dialog
    if (state.isNameDialogOpen) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(SettingsEvent.ShowNameDialog(false)) },
            title = { Text(stringResource(R.string.settings_update_name), color = HealthBlueDark, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = state.tempUserName,
                    onValueChange = { viewModel.onEvent(SettingsEvent.UpdateTempName(it)) },
                    label = { Text(stringResource(R.string.settings_enter_name)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HealthBlue,
                        focusedLabelColor = HealthBlue
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(SettingsEvent.SaveUserName) }) {
                    Text(stringResource(R.string.settings_save), color = HealthBlueDark, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(SettingsEvent.ShowNameDialog(false)) }) {
                    Text(stringResource(R.string.settings_cancel), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }

    // Handle Language Selection Dialog
    if (state.isLanguageDialogOpen) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(SettingsEvent.ShowLanguageDialog(false)) },
            title = { Text(stringResource(R.string.settings_app_language), color = HealthBlueDark, fontWeight = FontWeight.Bold) },
            text = {
                LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 320.dp)) {
                    items(languages.size) { index ->
                        val (tag, name) = languages[index]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.onEvent(SettingsEvent.SelectLanguage(tag)) }
                                .padding(vertical = 10.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (tag == state.selectedLanguageTag),
                                onClick = { viewModel.onEvent(SettingsEvent.SelectLanguage(tag)) }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = name, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(SettingsEvent.ShowLanguageDialog(false)) }) {
                    Text(stringResource(R.string.settings_cancel), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(stringResource(R.string.settings_title), fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack, 
                                contentDescription = "Back",
                                tint = HealthBlueDark
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = HealthBlueDark
                    ),
                    modifier = Modifier.shadow(elevation = 2.dp)
                )
                HorizontalDivider(
                    modifier = Modifier,
                    thickness = 0.25.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            // --- Account Section ---
            item { SettingsHeader(stringResource(R.string.settings_section_account)) }
            item {
                SettingsItem(
                    title = stringResource(R.string.settings_profile),
                    subtitle = state.userName.ifEmpty { state.userEmail },
                    icon = Icons.Default.Person,
                    onClick = { viewModel.onEvent(SettingsEvent.ShowNameDialog(true)) }
                )
            }
            item {
                SettingsItem(
                    title = stringResource(R.string.settings_view_medical_records),
                    subtitle = stringResource(R.string.settings_verify_identity_subtitle),
                    icon = Icons.Default.Visibility,
                    onClick = {
                        if (promptManager != null) {
                            promptManager.showBiometricPrompt(
                                title = "Verify Identity",
                                description = "Authenticate to view your medical records"
                            )
                        } else {
                            Toast.makeText(context, "Biometric authentication not available", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
            item {
                SettingsItem(
                    title = stringResource(R.string.settings_sign_out),
                    subtitle = stringResource(R.string.settings_sign_out_subtitle),
                    icon = Icons.Default.Logout,
                    textColor = MaterialTheme.colorScheme.error,
                    onClick = {
                        viewModel.onEvent(SettingsEvent.SignOut)
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            item { HorizontalDivider(Modifier.padding(vertical = 8.dp), color = HealthBlue.copy(alpha = 0.15f)) }

            // --- Preferences Section ---
            item { SettingsHeader(stringResource(R.string.settings_section_preferences)) }
            item {
                SettingsToggleItem(
                    title = stringResource(R.string.settings_dark_mode),
                    subtitle = stringResource(R.string.settings_dark_mode_subtitle),
                    icon = Icons.Default.DarkMode,
                    checked = state.isDarkMode,
                    onCheckedChange = { viewModel.onEvent(SettingsEvent.ToggleDarkMode(it)) }
                )
            }
            item {
                SettingsItem(
                    title = stringResource(R.string.settings_app_language),
                    subtitle = currentLangName,
                    icon = Icons.Default.Language,
                    onClick = { viewModel.onEvent(SettingsEvent.ShowLanguageDialog(true)) }
                )
            }

            item { HorizontalDivider(Modifier.padding(vertical = 8.dp), color = HealthBlue.copy(alpha = 0.15f)) }

            // --- AI Configuration Section ---
            item { SettingsHeader(stringResource(R.string.settings_section_ai)) }
            item {
                SettingsItem(
                    title = stringResource(R.string.settings_gemini_api_key),
                    subtitle = if (state.hasApiKey) stringResource(R.string.settings_api_configured) else stringResource(R.string.settings_api_tap_to_configure),
                    icon = Icons.Default.VpnKey,
                    onClick = { 
                        navController.navigate(Screen.ApiKeySettings.route) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            if (state.hasApiKey) {
                item {
                    SettingsItem(
                        title = stringResource(R.string.settings_reset_api_key),
                        subtitle = stringResource(R.string.settings_reset_api_key_subtitle),
                        icon = Icons.Default.Refresh,
                        onClick = { viewModel.onEvent(SettingsEvent.ClearApiKey) }
                    )
                }
            }

            item { HorizontalDivider(Modifier.padding(vertical = 8.dp), color = HealthBlue.copy(alpha = 0.15f)) }

            // --- About Section ---
            item { SettingsHeader(stringResource(R.string.settings_section_about)) }
            item {
                SettingsItem(
                    title = stringResource(R.string.settings_version),
                    subtitle = state.appVersion,
                    icon = Icons.Default.Info
                )
            }
            item {
                SettingsItem(
                    title = stringResource(R.string.settings_privacy_policy),
                    subtitle = stringResource(R.string.settings_privacy_policy_subtitle),
                    icon = Icons.Default.PrivacyTip,
                    onClick = {
                        navController.navigate(Screen.PrivacyPolicy.route) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingsHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = HealthBlueDark,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun SettingsItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: (() -> Unit)? = null
) {
    Surface(
        onClick = { onClick?.invoke() },
        enabled = onClick != null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (onClick == null) MaterialTheme.colorScheme.onSurfaceVariant 
                       else if (textColor != MaterialTheme.colorScheme.onSurface) textColor
                       else HealthBlue
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, color = textColor)
                Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (onClick != null) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SettingsToggleItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon, 
                null, 
                Modifier.size(24.dp), 
                tint = if (checked) HealthBlue else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(
                checked = checked, 
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = HealthBlue,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    SettingsScreen(navController = rememberNavController())
}
