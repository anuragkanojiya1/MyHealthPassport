package com.example.myhealthpassport.Navigation

import  AgentScreen
import com.example.myhealthpassport.viewmodels.AgentViewModel
import android.util.Log
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myhealthpassport.FlipAnimation
import com.example.myhealthpassport.ui.composables.EmergencyContactsListPreview
import com.example.myhealthpassport.ui.composables.GetHealthInfo
import com.example.myhealthpassport.ui.composables.HealthAiScreen
import com.example.myhealthpassport.ui.composables.HealthInfo
import com.example.myhealthpassport.viewmodels.HealthViewModel
import com.example.myhealthpassport.ui.composables.NavigationDrawer
import com.example.myhealthpassport.ui.composables.PatientDetails
import com.example.myhealthpassport.ui.composables.SplashScreen
import com.example.myhealthpassport.ui.screens.SignInScreen
import com.example.myhealthpassport.ui.screens.SignUpScreen
import com.example.myhealthpassport.viewmodels.AiViewModel
import com.example.myhealthpassport.ui.composables.ChartScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(navController: NavHostController,
             healthViewModel: HealthViewModel,
             aiViewModel: AiViewModel,
             agentViewModel: AgentViewModel,
) {
    val context = LocalContext.current
//    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()

    NavHost(navController, startDestination = Screen.SplashScreen.route) {
        composable(Screen.SignUp.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            SignUpScreen(navController, auth)
        }
//        composable(Screen.ChatPage.route,
//            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
//            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }){
//            ChatPage(navController,context, chatViewModel)
//        }

        composable(Screen.Login.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            SignInScreen(navController, auth)
        }
//        composable(Screen.DoctorLogin.route,
//            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
//            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }){
//            DoctorLogin(navController, auth)
//        }
//        composable(Screen.Home.route) {
//            HomeScreen()
//        }
//        composable(Screen.MainHealthActivity.route,
//            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
//            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }){
//            MainHealthActivity(navController = navController)
//        }
        composable(Screen.HealthInfo.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            NavigationDrawer(navController = navController) {
                HealthInfo(navController = navController, healthViewModel = healthViewModel)
            }
        }

        composable(Screen.GetHealthInfo.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            NavigationDrawer(navController = navController) {
                GetHealthInfo(navController = navController, healthViewModel = healthViewModel)
            }
        }

        composable(
            Screen.PatientDetails.route,
            arguments = listOf(navArgument("patientData") {
                type = NavType.StringArrayType
            }),
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }
        ) {
            val patientData = it.arguments?.getStringArray("patientData")?.toList() ?: emptyList()
            Log.d("Args", patientData.toString())
            NavigationDrawer(navController = navController) {
                PatientDetails(navController = navController, patientData = patientData)
            }
        }


        composable(Screen.EmergencyContacts.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            NavigationDrawer(navController = navController) {
                EmergencyContactsListPreview(navController = navController)
            }
        }
        composable(Screen.SplashScreen.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() })
        {
            SplashScreen(navController = navController)
        }
        composable(Screen.HealthAiScreen.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            NavigationDrawer(navController = navController) {
                HealthAiScreen(navController = navController, aiViewModel = aiViewModel)
            }
        }
        composable(Screen.FlipAnimation.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            NavigationDrawer(navController = navController) {
                FlipAnimation(navController = navController)
            }
        }

//        composable(Screen.NavigationDrawer.route,
//            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
//            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }){
//            NavigationDrawer(navController = navController){
//                FlipAnimation(navController = navController)
//            }
//        }

        composable(Screen.AgentScreen.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }
            ) {
            NavigationDrawer(navController = navController) {
                AgentScreen(
                    navController = navController,
                    agentViewModel = agentViewModel,
                    healthViewModel = healthViewModel
                )
            }
        }
        composable(Screen.ChartScreen.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            NavigationDrawer(navController = navController) {
                ChartScreen(navController = navController)
            }
        }

    }
}

@Composable
fun HomeScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "HELLO", color = Color.Black)
        }
    }
}
