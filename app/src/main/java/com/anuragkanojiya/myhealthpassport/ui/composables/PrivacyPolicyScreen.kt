package com.anuragkanojiya.myhealthpassport.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.anuragkanojiya.myhealthpassport.ui.theme.HealthBlueDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy Policy", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = HealthBlueDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = HealthBlueDark
                ),
                modifier = Modifier.shadow(elevation = 2.dp)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "MyHealthPassport Privacy Policy",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = HealthBlueDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Effective date: July 30, 2026",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "MyHealthPassport (“we,” “our,” or “the App”) is committed to protecting your privacy. This Privacy Policy explains what information we collect, how we use it, when we share it, and the choices you have regarding your data.",
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            PrivacySection(
                title = "1. Information We Collect",
                content = "MyHealthPassport collects information needed to provide a secure, personalized digital health experience. This may include:\n" +
                        "• Account and authentication information, including email, sign-in details, and secure login state.\n" +
                        "• Medical profile information such as your digital medical ID, vitals, medication history, emergency contact details, and uploaded health reports.\n" +
                        "• Images, documents, and health data submitted for AI analysis, such as medical reports or scanned records.\n" +
                        "• Voice input, chat interactions, prompts, and AI-generated recommendations used to support health coaching and analysis.\n" +
                        "• Device, app usage, diagnostics, and performance information needed to improve reliability and security."
            )
            
            PrivacySection(
                title = "2. How We Use Your Information",
                content = "We use your information to deliver the core features of MyHealthPassport, including:\n" +
                        "• Creating and maintaining your personal health profile and medical ID.\n" +
                        "• Analyzing medical images and documents with AI to extract health metrics and update your records.\n" +
                        "• Providing personalized health coaching, diet and exercise recommendations, and health insights.\n" +
                        "• Tracking blood pressure, glucose, medication patterns, and other longitudinal health trends.\n" +
                        "• Supporting emergency access, voice interaction, text-to-speech, and other accessibility features.\n" +
                        "• Improving app reliability, user experience, and security protections."
            )
            
            PrivacySection(
                title = "3. AI Processing and Health Data",
                content = "Some features use AI services, including Google Gemini AI, to interpret medical documents, images, and health-related inputs. This may involve transmitting uploaded records, diagnostic information, or user-entered health details to approved AI processing services so the App can generate summaries, recommendations, and insights. We use this information only to provide the requested health analysis and related features described in the App."
            )
            
            PrivacySection(
                title = "4. Security and Data Protection",
                content = "We prioritize the protection of sensitive health and personal data. MyHealthPassport uses multiple layers of security, including:\n" +
                        "• Biometric authentication via Android Biometric API for fingerprint or face-based access to sensitive data.\n" +
                        "• AES-256 encryption through the Android Keystore and CryptoManager for storing sensitive medical information.\n" +
                        "• Secure authentication from Firebase Auth and Google Identity Services.\n" +
                        "• Protected cloud and app storage for medical profiles, reports, and account information.\n\n" +
                        "While we use commercially reasonable safeguards, no system is completely immune to risk. We continuously work to reduce risks associated with unauthorized access, disclosure, or misuse of health data."
            )
            
            PrivacySection(
                title = "5. How We Share Your Information",
                content = "We do not sell personal data. We may share information only when needed to operate the App or comply with legal obligations, including:\n" +
                        "• With trusted service providers that support authentication, storage, analytics, and AI processing.\n" +
                        "• With Firebase and related infrastructure providers needed to secure and deliver the App’s services.\n" +
                        "• When required by law, court order, or legitimate safety or legal process.\n" +
                        "• To protect the rights, safety, or property of users or the public."
            )
            
            PrivacySection(
                title = "6. Device, Widget, and Accessibility Data",
                content = "If you use the App’s home screen widget, voice features, or text-to-speech functionality, we may process limited data necessary to display health summaries and support accessibility needs. This data is used to provide the App’s intended functionality and improve the usability of your health dashboard and notifications."
            )
            
            PrivacySection(
                title = "7. Your Choices and Rights",
                content = "Depending on your location, you may have rights to access, correct, delete, or restrict the processing of your personal and health data. You may also be able to manage certain account settings within the App or request assistance by contacting us. If you need to delete your account or remove health data, please reach out using the contact information below so we can help."
            )
            
            PrivacySection(
                title = "8. Children’s Privacy",
                content = "MyHealthPassport is not intended for children under the age of 13, and we do not knowingly collect personal information from children under 13 without appropriate parental consent. If you believe a child has provided us with personal information, please contact us and we will take steps to remove it promptly."
            )
            
            PrivacySection(
                title = "9. Changes to This Policy",
                content = "We may update this Privacy Policy from time to time to reflect changes in the App, regulatory requirements, or our security practices. Any updates will be posted on this page with a revised effective date."
            )
            
            PrivacySection(
                title = "10. Contact Us",
                content = "If you have questions about this Privacy Policy, your personal data, or the App’s security practices, please contact us at myhealthpassport.support@gmail.com."
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun PrivacySection(title: String, content: String) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
