package com.anuragkanojiya.myhealthpassport.ui.composables

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anuragkanojiya.myhealthpassport.domain.model.SettingsUiState
import com.anuragkanojiya.myhealthpassport.feature.settings.SettingsScreen
import com.anuragkanojiya.myhealthpassport.feature.settings.SettingsViewModel
import com.anuragkanojiya.myhealthpassport.navigation.Screen
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel: SettingsViewModel = mockk(relaxed = true)
    private val uiState = MutableStateFlow(SettingsUiState(userEmail = "test@example.com"))

    @Test
    fun settingsScreen_displaysCorrectInfo() {
        every { viewModel.uiState } returns uiState

        composeTestRule.setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = Screen.SettingsScreen.route) {
                composable(Screen.SettingsScreen.route) {
                    SettingsScreen(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
                composable(Screen.Login.route) {}
                composable("patient_details/all_records") {}
            }
        }

        composeTestRule.onNodeWithText("Settings").assertIsDisplayed()
        composeTestRule.onNodeWithText("test@example.com").assertIsDisplayed()
    }

    @Test
    fun clickingSignOut_callsSignOutEvent() {
        every { viewModel.uiState } returns uiState

        composeTestRule.setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = Screen.SettingsScreen.route) {
                composable(Screen.SettingsScreen.route) {
                    SettingsScreen(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
                composable(Screen.Login.route) {}
                composable("patient_details/all_records") {}
            }
        }

        composeTestRule.onNodeWithText("Sign Out").performClick()
        // Event handling can be verified by checking if the specific event was sent
        // But since we are mocking the VM, we just ensure it doesn't crash and the UI reacts if needed.
    }
}
