package com.example.myhealthpassport.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myhealthpassport.ui.composables.*
import com.example.myhealthpassport.ui.screens.SignInScreen
import com.example.myhealthpassport.ui.screens.SignUpScreen
import com.example.myhealthpassport.util.BiometricPromptManager
import com.example.myhealthpassport.viewmodels.AgentViewModel
import com.example.myhealthpassport.viewmodels.AiViewModel
import com.example.myhealthpassport.viewmodels.ApiKeyViewModel
import com.example.myhealthpassport.viewmodels.HealthViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(
    navController: NavHostController,
    healthViewModel: HealthViewModel = hiltViewModel(),
    aiViewModel: AiViewModel = hiltViewModel(),
    agentViewModel: AgentViewModel = hiltViewModel(),
    apiKeyViewModel: ApiKeyViewModel = hiltViewModel(),
    promptManager: BiometricPromptManager
) {

    val auth = FirebaseAuth.getInstance()

    NavHost(navController, startDestination = Screen.SplashScreen.route) {

        composable(
            Screen.SignUp.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            SignUpScreen(navController, auth)
        }

        composable(
            Screen.Login.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            SignInScreen(navController, auth)
        }

        composable(
            Screen.HealthInfo.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            NavigationDrawer(navController = navController) {
                HealthInfo(
                    navController = navController,
                    healthViewModel = hiltViewModel()
                )
            }
        }

        composable(
            Screen.GetHealthInfo.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            NavigationDrawer(navController = navController) {
                GetHealthInfo(
                    navController = navController,
                    healthViewModel = hiltViewModel()
                )
            }
        }

        composable(
            Screen.PatientDetails.route,
            arguments = listOf(navArgument("patientData") { type = NavType.StringType }),
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }
        ) { backStackEntry ->
            val patientData = backStackEntry.arguments?.getString("patientData") ?: ""

            PatientDetails(
                navController = navController,
                patientData = patientData,
                healthViewModel = hiltViewModel()
            )
        }

        composable(
            Screen.EmergencyContacts.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            NavigationDrawer(navController = navController) {
                EmergencyContactsScreen(navController = navController)
            }
        }

        composable(
            Screen.ApiKeySettings.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            NavigationDrawer(navController = navController) {
                ApiKeySettingsScreen(navController = navController, viewModel = hiltViewModel())
            }
        }

        composable(Screen.SplashScreen.route) {
            SplashScreen(navController = navController)
        }

        composable(
            Screen.HealthAiScreen.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            NavigationDrawer(navController = navController) {
                HealthAiScreen(
                    navController = navController,
                    aiViewModel = hiltViewModel(),
                    apiKeyViewModel = hiltViewModel()
                )
            }
        }

        composable(
            Screen.FlipAnimation.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            NavigationDrawer(navController = navController) {
                HomeScreen(navController = navController)
            }
        }

        composable(
            Screen.AgentScreen.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }
        ) {
            NavigationDrawer(navController = navController) {
                AgentScreen(
                    navController = navController,
                    agentViewModel = hiltViewModel(),
                    healthViewModel = hiltViewModel()
                )
            }
        }

        composable(
            Screen.ChartScreen.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            NavigationDrawer(navController = navController) {
                ChartScreen(navController = navController)
            }
        }

        composable(
            Screen.SettingsScreen.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            SettingsScreen(
                navController = navController,
                promptManager = promptManager
            )
        }
    }
}
