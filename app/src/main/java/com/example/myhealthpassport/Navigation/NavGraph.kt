package com.example.myhealthpassport.Navigation

import  AgentScreen
import com.example.myhealthpassport.ViewModels.AgentViewModel
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
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myhealthpassport.FlipAnimation
import com.example.myhealthpassport.Composables.EmergencyContactsListPreview
import com.example.myhealthpassport.Composables.GetHealthInfo
import com.example.myhealthpassport.Composables.HealthAiScreen
import com.example.myhealthpassport.Composables.HealthInfo
import com.example.myhealthpassport.ViewModels.HealthViewModel
import com.example.myhealthpassport.Composables.NavigationDrawer
import com.example.myhealthpassport.Composables.PatientDetails
import com.example.myhealthpassport.Composables.SplashScreen
import com.example.myhealthpassport.SignInSignUp.DoctorLogin
import com.example.myhealthpassport.SignInSignUp.SignInScreen
import com.example.myhealthpassport.SignInSignUp.SignUpScreen
import com.example.myhealthpassport.ViewModels.AiViewModel
import com.example.myhealthpassport.aicompanion.ChatPage
import com.example.myhealthpassport.aicompanion.ChatViewModel
import com.example.myhealthpassport.graph.ChartScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(navController: NavController,
             healthViewModel: HealthViewModel,
             aiViewModel: AiViewModel,
             agentViewModel: AgentViewModel,
             chatViewModel: ChatViewModel
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()

    NavHost(navController, startDestination = Screen.SplashScreen.route) {
        composable(Screen.SignUp.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            SignUpScreen(navController, auth)
        }
        composable(Screen.ChatPage.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }){
            ChatPage(navController,context, chatViewModel)
        }
        composable(Screen.ChartScreen.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }){

        }
        composable(Screen.Login.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }) {
            SignInScreen(navController, auth)
        }
        composable(Screen.DoctorLogin.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }){
            DoctorLogin(navController, auth)
        }
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
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }){
            HealthInfo(navController = navController, healthViewModel = healthViewModel)
        }
        composable(Screen.GetHealthInfo.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }){
            GetHealthInfo(navController = navController, healthViewModel = healthViewModel)
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
            PatientDetails(navController = navController, patientData = patientData)
        }


        composable(Screen.EmergencyContacts.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }){
            EmergencyContactsListPreview(navController = navController)
        }
        composable(Screen.SplashScreen.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() })
        {
            SplashScreen(navController = navController)
        }
        composable(Screen.HealthAiScreen.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }){
            HealthAiScreen(navController = navController, aiViewModel = aiViewModel)
        }
        composable(Screen.FlipAnimation.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }){
            FlipAnimation(navController = navController)
        }
        composable(Screen.NavigationDrawer.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }){
            NavigationDrawer(navController = navController)
        }

        composable(Screen.AgentScreen.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }
            ){
            AgentScreen(
                navController = navController,
                agentViewModel = agentViewModel,
                healthViewModel = healthViewModel)
        }
        composable(Screen.ChartScreen.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }){
            ChartScreen(navController = navController)
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
