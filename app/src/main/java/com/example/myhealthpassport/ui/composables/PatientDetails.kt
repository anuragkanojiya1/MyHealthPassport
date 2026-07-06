package com.example.myhealthpassport.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myhealthpassport.R
import com.example.myhealthpassport.domain.model.UserHealthData
import com.example.myhealthpassport.viewmodels.HealthViewModel
import com.example.myhealthpassport.data.repository.HealthRepositoryImpl
import com.example.myhealthpassport.ui.theme.HealthBlue
import com.example.myhealthpassport.ui.theme.HealthBlueDark
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetails(
    navController: NavController,
    patientData: String,
    healthViewModel: HealthViewModel
) {
    var records by remember { mutableStateOf<List<UserHealthData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(patientData) {
        isLoading = true
        if (patientData.isNotBlank() && patientData != "all_records") {
            healthViewModel.retrieveHealthData(patientData, context) { data ->
                records = listOf(data)
                isLoading = false
            }
        } else {
            healthViewModel.fetchAllHealthData(context) { dataList ->
                records = dataList
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = if (patientData != "all_records") "Record Details" else "Medical History",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = HealthBlueDark
                    ),
                    windowInsets = WindowInsets.statusBars
                )
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Subtle Background Image
            Image(
                painter = painterResource(id = R.drawable.healthcare),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.15f
            )

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = HealthBlue)
                }
            } else if (records.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No records found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = HealthBlueDark
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(records, key = { it.medicalID }) { data ->
                        MedicalRecordCard(data)
                    }
                }
            }
        }
    }
}

@Composable
fun MedicalRecordCard(data: UserHealthData) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 400.dp)
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(listOf(HealthBlue.copy(alpha = 0.5f), HealthBlueDark.copy(alpha = 0.5f))),
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
            .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.HealthAndSafety,
                    contentDescription = null,
                    tint = HealthBlue,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = data.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = HealthBlueDark
                    )
                    Text(
                        text = "ID: ${data.medicalID}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 1.dp,
                color = HealthBlue.copy(alpha = 0.2f)
            )

            // Details Grid
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Vital Stats Group
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = HealthBlue.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.weight(1f)) {
                                DetailRow(Icons.Default.Person, "Age", "${data.age} yrs")
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                DetailRow(Icons.Default.Bloodtype, "Blood", data.bloodGroup)
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.weight(1f)) {
                                DetailRow(Icons.Default.Wc, "Gender", data.gender)
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                DetailRow(Icons.Default.MonitorWeight, "Weight", "${data.weight} kg")
                            }
                        }
                        DetailRow(Icons.Default.Height, "Height", "${data.height} m")
                    }
                }

                // Medical Data
                DetailRow(Icons.Default.Speed, "Blood Pressure", "${data.systolicBP}/${data.diastolicBP} mmHg")
                DetailRow(Icons.Default.WaterDrop, "Blood Sugar", "${data.bloodSugarLevel} mg/dL")
                DetailRow(Icons.Default.MedicalServices, "Condition", data.healthCondition)
                
                // Safety Info
                DetailRow(Icons.Default.Warning, "Allergies", data.allergies)
                DetailRow(Icons.Default.Medication, "Medications", data.medications)
                
                // Contact Info
                DetailRow(Icons.Default.Home, "Address", data.address)
                DetailRow(Icons.Default.Phone, "Emergency Phone", data.emergencyPhoneNumber)
            }
        }
    }
}

@Composable
fun DetailRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = HealthBlueDark.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = HealthBlue
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PatientPreview() {
    // Mock record for preview
    val mockRecords = listOf(
        UserHealthData(
            medicalID = "ID12345",
            name = "John Doe",
            bloodGroup = "A+",
            age = 30,
            weight = 75f,
            height = 1.8f,
            gender = "Male",
            systolicBP = 120,
            diastolicBP = 80,
            bloodSugarLevel = 95,
            healthCondition = "Excellent",
            emergencyPhoneNumber = "9876543210",
            address = "123 Health Street",
            allergies = "Peanuts",
            medications = "None"
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(mockRecords) { data ->
                MedicalRecordCard(data)
            }
        }
    }
}
