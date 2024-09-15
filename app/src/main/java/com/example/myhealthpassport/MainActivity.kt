package com.example.myhealthpassport

import AgentViewModel
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myhealthpassport.ViewModels.HealthViewModel
import com.example.myhealthpassport.Navigation.NavGraph
import com.example.myhealthpassport.ViewModels.AiViewModel
import com.example.myhealthpassport.ui.theme.MyHealthPassportTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    private lateinit var navController : NavController
    private val healthViewModel: HealthViewModel by viewModels()
    private val aiViewModel: AiViewModel by viewModels()
    private val agentViewModel: AgentViewModel by viewModels()

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db: FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        FirebaseApp.initializeApp(this)
        setContent {
            MyHealthPassportTheme {
                val navController = rememberNavController()

                NavGraph(navController = navController,
                    healthViewModel = healthViewModel,
                    aiViewModel = aiViewModel,
                    agentViewModel = agentViewModel)
            }
        }
    }
}
