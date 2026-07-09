package com.example.myhealthpassport.ui.composables

import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myhealthpassport.util.BiometricPromptManager
import com.example.myhealthpassport.domain.model.SettingsEvent
import com.example.myhealthpassport.ui.navigation.Screen
import com.example.myhealthpassport.ui.theme.HealthBlue
import com.example.myhealthpassport.ui.theme.HealthBlueDark
import com.example.myhealthpassport.viewmodels.SettingsViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    // Handle Name Edit Dialog
    if (state.isNameDialogOpen) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(SettingsEvent.ShowNameDialog(false)) },
            title = { Text("Update Name", color = HealthBlueDark, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = state.tempUserName,
                    onValueChange = { viewModel.onEvent(SettingsEvent.UpdateTempName(it)) },
                    label = { Text("Enter your name") },
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
                    Text("Save", color = HealthBlueDark, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(SettingsEvent.ShowNameDialog(false)) }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Settings", fontWeight = FontWeight.Bold) },
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
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(1.dp)
//                        .background(Brush.horizontalGradient(listOf(HealthBlue, HealthBlueDark)))
//                )
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
            item { SettingsHeader("Account") }
            item {
                SettingsItem(
                    title = "Profile",
                    subtitle = state.userName.ifEmpty { state.userEmail },
                    icon = Icons.Default.Person,
                    onClick = { viewModel.onEvent(SettingsEvent.ShowNameDialog(true)) }
                )
            }
            item {
                SettingsItem(
                    title = "View My Medical Records",
                    subtitle = "Verify identity to see all IDs",
                    icon = Icons.Default.Visibility,
                    onClick = {
                        if (promptManager != null) {
                            promptManager.showBiometricPrompt(
                                title = "Verify Identity",
                                description = "Authenticate to view your medical records"
                            )
                        } else {
                            // Fallback if promptManager is null (e.g. in preview or if not initialized)
                            Toast.makeText(context, "Biometric authentication not available", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
            item {
                SettingsItem(
                    title = "Sign Out",
                    subtitle = "Logout from your session",
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
            item { SettingsHeader("Preferences") }
            item {
                SettingsToggleItem(
                    title = "Dark Mode",
                    subtitle = "Enable dark theme for the app",
                    icon = Icons.Default.DarkMode,
                    checked = state.isDarkMode,
                    onCheckedChange = { viewModel.onEvent(SettingsEvent.ToggleDarkMode(it)) }
                )
            }

            item { HorizontalDivider(Modifier.padding(vertical = 8.dp), color = HealthBlue.copy(alpha = 0.15f)) }

            // --- AI Configuration Section ---
            item { SettingsHeader("AI Services") }
            item {
                SettingsItem(
                    title = "Gemini API Key",
                    subtitle = if (state.hasApiKey) "Configured ✓" else "Tap to configure",
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
                        title = "Reset API Key",
                        subtitle = "Remove key from local storage",
                        icon = Icons.Default.Refresh,
                        onClick = { viewModel.onEvent(SettingsEvent.ClearApiKey) }
                    )
                }
            }

            item { HorizontalDivider(Modifier.padding(vertical = 8.dp), color = HealthBlue.copy(alpha = 0.15f)) }

            // --- About Section ---
            item { SettingsHeader("About") }
            item {
                SettingsItem(
                    title = "Version",
                    subtitle = state.appVersion,
                    icon = Icons.Default.Info
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
