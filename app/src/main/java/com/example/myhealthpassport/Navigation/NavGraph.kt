package com.example.myhealthpassport.Navigation

import AgentScreen
import AgentViewModel
import FileUploadDownloadScreen
import com.example.myhealthpassport.ViewModels.FileViewModel
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myhealthpassport.FlipAnimation
import com.example.myhealthpassport.Health.ChatScreen
import com.example.myhealthpassport.Health.EmergencyContactsListPreview
import com.example.myhealthpassport.Health.GetHealthInfo
import com.example.myhealthpassport.Health.HealthAiScreen
import com.example.myhealthpassport.Health.HealthInfo
import com.example.myhealthpassport.ViewModels.HealthViewModel
import com.example.myhealthpassport.Health.MainHealthActivity
import com.example.myhealthpassport.Health.NavigationDrawer
import com.example.myhealthpassport.Health.PatientDetails
import com.example.myhealthpassport.Health.SplashScreen
import com.example.myhealthpassport.SignInSignUp.DoctorLogin
import com.example.myhealthpassport.SignInSignUp.SignInScreen
import com.example.myhealthpassport.SignInSignUp.SignUpScreen
import com.example.myhealthpassport.ViewModels.AiViewModel
import com.example.myhealthpassport.ViewModels.ChatViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(navController: NavController,
             healthViewModel: HealthViewModel,
             aiViewModel: AiViewModel,
             agentViewModel: AgentViewModel) {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()

    NavHost(navController, startDestination = Screen.SplashScreen.route) {
        composable(Screen.SignUp.route) {
            SignUpScreen(navController, auth)
        }
        composable(Screen.Login.route) {
            SignInScreen(navController, auth)
        }
        composable(Screen.DoctorLogin.route){
            DoctorLogin(navController, auth)
        }
//        composable(Screen.Home.route) {
//            HomeScreen()
//        }
        composable(Screen.MainHealthActivity.route){
            MainHealthActivity(navController = navController)
        }
        composable(Screen.HealthInfo.route){
            HealthInfo(navController = navController, healthViewModel = healthViewModel)
        }
        composable(Screen.GetHealthInfo.route){
            GetHealthInfo(navController = navController, healthViewModel = healthViewModel)
        }

        composable(
            Screen.PatientDetails.route,
            arguments = listOf(navArgument("patientData") {
                type = NavType.StringArrayType
            })
        ) {
            val patientData = it.arguments?.getStringArray("patientData")?.toList() ?: emptyList()
            Log.d("Args", patientData.toString())
            PatientDetails(navController = navController, patientData = patientData)
        }


        composable(Screen.EmergencyContacts.route){
            EmergencyContactsListPreview(navController = navController)
        }
        composable(Screen.SplashScreen.route)
        {
            SplashScreen(navController = navController)
        }
        composable(Screen.HealthAiScreen.route){
            HealthAiScreen(navController = navController, aiViewModel = aiViewModel)
        }
        composable(Screen.FlipAnimation.route){
            FlipAnimation(navController = navController)
        }
        composable(Screen.NavigationDrawer.route){
            NavigationDrawer(navController = navController)
        }
        composable(Screen.ChatScreen.route){
            ChatScreen(navController = navController, viewModel = ChatViewModel())
        }
        composable(Screen.AgentScreen.route){
            AgentScreen(
                navController = navController,
                agentViewModel = agentViewModel,
                healthViewModel = healthViewModel)
        }
        composable(Screen.FileUploadDownloadScreen.route){
            FileUploadDownloadScreen(navController = navController, viewModel = FileViewModel())
        }
//        composable(Screen.AddDataScreen.route){
//            AddDataScreen(navController = navController, sharedViewModel = sharedViewModel)
//        }
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
