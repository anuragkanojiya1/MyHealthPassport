package com.anuragkanojiya.myhealthpassport.navigation

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
import com.anuragkanojiya.myhealthpassport.ui.screens.SignInScreen
import com.anuragkanojiya.myhealthpassport.ui.screens.SignUpScreen
import com.anuragkanojiya.myhealthpassport.core.security.BiometricPromptManager
import com.anuragkanojiya.myhealthpassport.feature.ai.AgentScreen
import com.anuragkanojiya.myhealthpassport.feature.medicalreport.HealthAiScreen
import com.anuragkanojiya.myhealthpassport.feature.health.HealthInfo
import com.anuragkanojiya.myhealthpassport.feature.home.HomeScreen
import com.anuragkanojiya.myhealthpassport.feature.ai.AgentViewModel
import com.anuragkanojiya.myhealthpassport.feature.ai.AiViewModel
import com.anuragkanojiya.myhealthpassport.feature.health.PatientDetails
import com.anuragkanojiya.myhealthpassport.feature.emergency.EmergencyContactsScreen
import com.anuragkanojiya.myhealthpassport.feature.settings.ApiKeyViewModel
import com.anuragkanojiya.myhealthpassport.feature.health.HealthViewModel
import com.anuragkanojiya.myhealthpassport.feature.dashboard.ChartScreen
import com.anuragkanojiya.myhealthpassport.feature.health.GetHealthInfo
import com.anuragkanojiya.myhealthpassport.feature.home.NavigationDrawer
import com.anuragkanojiya.myhealthpassport.feature.home.SplashScreen
import com.anuragkanojiya.myhealthpassport.feature.privacy.PrivacyPolicyScreen
import com.anuragkanojiya.myhealthpassport.feature.settings.ApiKeySettingsScreen
import com.anuragkanojiya.myhealthpassport.feature.settings.SettingsScreen
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
            ApiKeySettingsScreen(navController = navController, viewModel = hiltViewModel())
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

        composable(
            Screen.PrivacyPolicy.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            PrivacyPolicyScreen(navController = navController)
        }
    }
}
