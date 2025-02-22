package com.example.myhealthpassport

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.myhealthpassport.ViewModels.AgentViewModel
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myhealthpassport.ViewModels.HealthViewModel
import com.example.myhealthpassport.Navigation.NavGraph
import com.example.myhealthpassport.ViewModels.AiViewModel
import com.example.myhealthpassport.aicompanion.ChatViewModel
import com.example.myhealthpassport.ui.theme.MyHealthPassportTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Locale

class MainActivity : ComponentActivity() {
    var speechInput = mutableStateOf("")

    private lateinit var navController : NavController
    private val chatViewModel: ChatViewModel by viewModels()
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
                    agentViewModel = agentViewModel,
                    chatViewModel = ChatViewModel(this)
                    )
            }
        }
    }

    fun askSpeechInput(context: Context) {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            Toast.makeText(context, "Speech not Available", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Talk")
            startActivityForResult(intent, 102)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 102 && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            speechInput.value = result?.get(0).toString()
        }
    }

}
