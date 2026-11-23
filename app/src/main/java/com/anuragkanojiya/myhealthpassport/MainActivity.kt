package com.anuragkanojiya.myhealthpassport

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalConfiguration
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
import com.google.firebase.auth.FirebaseAuth
import com.anuragkanojiya.myhealthpassport.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import java.util.Locale
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

        handleIntent(intent)

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
            val baseConfiguration = LocalConfiguration.current
            val appConfiguration = remember(
                settingsState.selectedLanguageTag,
                baseConfiguration
            ) {
                val locale = if (settingsState.selectedLanguageTag.isBlank()) {
                    Resources.getSystem().configuration.locales[0]
                } else {
                    Locale.forLanguageTag(settingsState.selectedLanguageTag)
                }
                Locale.setDefault(locale)
                Configuration(baseConfiguration).apply {
                    setLocale(locale)
                    setLayoutDirection(locale)
                }
            }

            CompositionLocalProvider(LocalConfiguration provides appConfiguration) {
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

                    val navigateToHealthAi by aiViewModel.navigateToHealthAi.collectAsState()
                    LaunchedEffect(navigateToHealthAi) {
                        if (navigateToHealthAi) {
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            if (currentUser != null) {
                                navController.navigate(Screen.HealthAiScreen.route) {
                                    popUpTo(Screen.FlipAnimation.route) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                            aiViewModel.onNavigatedToHealthAi()
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
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND && intent.type?.startsWith("image/") == true) {
            val imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
            }
            imageUri?.let { uri ->
                aiViewModel.loadSharedImage(uri, contentResolver)
            }
        }
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
