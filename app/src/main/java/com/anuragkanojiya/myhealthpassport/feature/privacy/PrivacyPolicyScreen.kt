package com.anuragkanojiya.myhealthpassport.feature.privacy

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
import androidx.compose.ui.res.stringResource
import com.anuragkanojiya.myhealthpassport.R
import com.anuragkanojiya.myhealthpassport.core.designsystem.theme.HealthBlueDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_privacy_policy), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
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
                text = stringResource(R.string.privacy_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = HealthBlueDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.privacy_effective_date),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.privacy_intro),
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            PrivacySection(
                title = stringResource(R.string.privacy_sec1_title),
                content = stringResource(R.string.privacy_sec1_body)
            )
            
            PrivacySection(
                title = stringResource(R.string.privacy_sec2_title),
                content = stringResource(R.string.privacy_sec2_body)
            )
            
            PrivacySection(
                title = stringResource(R.string.privacy_sec3_title),
                content = stringResource(R.string.privacy_sec3_body)
            )
            
            PrivacySection(
                title = stringResource(R.string.privacy_sec4_title),
                content = stringResource(R.string.privacy_sec4_body)
            )
            
            PrivacySection(
                title = stringResource(R.string.privacy_sec5_title),
                content = stringResource(R.string.privacy_sec5_body)
            )
            
            PrivacySection(
                title = stringResource(R.string.privacy_sec6_title),
                content = stringResource(R.string.privacy_sec6_body)
            )
            
            PrivacySection(
                title = stringResource(R.string.privacy_sec7_title),
                content = stringResource(R.string.privacy_sec7_body)
            )
            
            PrivacySection(
                title = stringResource(R.string.privacy_sec8_title),
                content = stringResource(R.string.privacy_sec8_body)
            )
            
            PrivacySection(
                title = stringResource(R.string.privacy_sec9_title),
                content = stringResource(R.string.privacy_sec9_body)
            )
            
            PrivacySection(
                title = stringResource(R.string.privacy_sec10_title),
                content = stringResource(R.string.privacy_sec10_body)
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
