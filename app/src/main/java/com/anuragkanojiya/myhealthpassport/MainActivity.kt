package com.anuragkanojiya.myhealthpassport

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.anuragkanojiya.myhealthpassport.data.local.datastore.UserPreferencesRepository
import com.anuragkanojiya.myhealthpassport.navigation.NavGraph
import com.anuragkanojiya.myhealthpassport.core.designsystem.theme.MyHealthPassportTheme
import com.anuragkanojiya.myhealthpassport.core.security.BiometricPromptManager
import com.anuragkanojiya.myhealthpassport.feature.ai.AgentViewModel
import com.anuragkanojiya.myhealthpassport.feature.ai.AiViewModel
import com.anuragkanojiya.myhealthpassport.feature.health.HealthViewModel
import com.anuragkanojiya.myhealthpassport.feature.settings.ApiKeyViewModel
import com.anuragkanojiya.myhealthpassport.feature.settings.SettingsViewModel
import com.anuragkanojiya.myhealthpassport.widget.HealthDataWorker
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // var speechInput = mutableStateOf("")

    @Inject
    lateinit var preferencesRepository: UserPreferencesRepository

    private val promptManager by lazy {
        BiometricPromptManager(this)
    }

    private val healthViewModel: HealthViewModel by viewModels()
    private val aiViewModel: AiViewModel by viewModels()
    private val agentViewModel: AgentViewModel by viewModels()
    private val apiKeyViewModel: ApiKeyViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    private val networkReceiver = NetworkReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val intentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(networkReceiver, intentFilter)

        lifecycleScope.launch {
            preferencesRepository.appLanguageFlow.collect { tag ->
                val currentTag = AppCompatDelegate.getApplicationLocales().toLanguageTags()
                if (currentTag != tag && !(currentTag.isEmpty() && tag.isEmpty())) {
                    val localeList = if (tag.isEmpty()) {
                        LocaleListCompat.getEmptyLocaleList()
                    } else {
                        LocaleListCompat.forLanguageTags(tag)
                    }
                    AppCompatDelegate.setApplicationLocales(localeList)
                }
            }
        }

        installSplashScreen()

        FirebaseApp.initializeApp(this)

        lifecycleScope.launch(Dispatchers.IO) {
            scheduleHealthWidgetWorker(applicationContext)
        }

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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        // You could handle deep linking here if you want to go to a specific screen
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(networkReceiver)
        } catch (_: Exception) {}
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
