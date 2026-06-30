package com.example.myhealthpassport

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.example.myhealthpassport.ui.navigation.NavGraph
import com.example.myhealthpassport.ui.theme.MyHealthPassportTheme
import com.example.myhealthpassport.util.BiometricPromptManager
import com.example.myhealthpassport.viewmodels.*
import com.example.myhealthpassport.widget.HealthDataWorker
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    var speechInput = mutableStateOf("")

    private val promptManager by lazy {
        BiometricPromptManager(this)
    }

    private val healthViewModel: HealthViewModel by viewModels()
    private val aiViewModel: AiViewModel by viewModels()
    private val agentViewModel: AgentViewModel by viewModels()
    private val apiKeyViewModel: ApiKeyViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        FirebaseApp.initializeApp(this)
        setContent {
            val settingsState by settingsViewModel.uiState.collectAsState()
            
            MyHealthPassportTheme(darkTheme = settingsState.isDarkMode) {
                val navController = rememberNavController()

                val enrollLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult(),
                    onResult = {
                        println("Activity result: $it")
                    }
                )

                LaunchedEffect(promptManager) {
                    promptManager.promptResults.collect { result ->
                        if (result is BiometricPromptManager.BiometricResult.AuthenticationNotSet) {
                            if (Build.VERSION.SDK_INT >= 30) {
                                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                                    putExtra(
                                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                                    )
                                }
                                enrollLauncher.launch(enrollIntent)
                            }
                        }
                    }
                }

                scheduleHealthWidgetWorker(this)

                NavGraph(
                    navController = navController,
                    healthViewModel = healthViewModel,
                    aiViewModel = aiViewModel,
                    agentViewModel = agentViewModel,
                    apiKeyViewModel = apiKeyViewModel,
                    promptManager = promptManager
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

    fun scheduleHealthWidgetWorker(context: Context) {
        val request = androidx.work.PeriodicWorkRequestBuilder<HealthDataWorker>(15, java.util.concurrent.TimeUnit.MINUTES)
            .build()

        androidx.work.WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "HealthWidgetWorker",
                androidx.work.ExistingPeriodicWorkPolicy.UPDATE,
                request
            )
    }
}
