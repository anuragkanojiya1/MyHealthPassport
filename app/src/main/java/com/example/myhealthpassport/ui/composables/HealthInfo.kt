package com.example.myhealthpassport.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myhealthpassport.R
import com.example.myhealthpassport.data.repository.HealthRepositoryImpl
import com.example.myhealthpassport.domain.model.UserHealthData
import com.example.myhealthpassport.ui.theme.HealthBlue
import com.example.myhealthpassport.ui.theme.HealthBlueDark
import com.example.myhealthpassport.viewmodels.HealthViewModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthInfo(navController: NavController, healthViewModel: HealthViewModel) {
    var medicalID by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var systolicBP by remember { mutableStateOf("") }
    var diastolicBP by remember { mutableStateOf("") }
    var bloodSugarLevel by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var healthCondition by remember { mutableStateOf("") }
    var emergencyPhoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var allergies by remember { mutableStateOf("") }
    var medications by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scrollView = rememberScrollState()
    val backgroundPainter: Painter = painterResource(id = R.drawable.healthcare)

    val gradient = Brush.horizontalGradient(
        colors = listOf(HealthBlue, HealthBlueDark)
    )

    // Automatically load existing health data if it exists
//    LaunchedEffect(Unit) {
//        val latestData = healthViewModel.getLatestHealthData(context)
//        latestData?.let {
//            medicalID = it.medicalID
//            name = it.name
//            bloodGroup = it.bloodGroup
//            age = it.age.toString()
//            systolicBP = it.systolicBP.toString()
//            diastolicBP = it.diastolicBP.toString()
//            bloodSugarLevel = it.bloodSugarLevel.toString()
//            weight = it.weight.toString()
//            height = it.height.toString()
//            gender = it.gender
//            healthCondition = it.healthCondition
//            emergencyPhoneNumber = it.emergencyPhoneNumber
//            address = it.address
//            allergies = it.allergies
//            medications = it.medications
//        }
//    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Image(
            painter = backgroundPainter,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.matchParentSize().alpha(0.35f),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(scrollView)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Update Health Profile",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )

            HealthInputField("Medical ID (Required)", medicalID, { medicalID = it })
            HealthInputField("Full Name", name, { name = it })
            
            Row(Modifier.fillMaxWidth()) {
                Box(Modifier.weight(1f)) {
                    HealthInputField("Blood Group", bloodGroup, { bloodGroup = it })
                }
                Spacer(Modifier.width(12.dp))
                Box(Modifier.weight(1f)) {
                    HealthInputField("Age", age, { age = it }, keyboardType = KeyboardType.Number)
                }
            }

            HorizontalDivider(
                Modifier.padding(vertical = 24.dp), 
                color = MaterialTheme.colorScheme.outlineVariant
            )
            
            Text(
                text = "Vitals", 
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold, 
                modifier = Modifier.align(Alignment.Start).padding(vertical = 8.dp), 
                color = HealthBlueDark
            )

            Row(Modifier.fillMaxWidth()) {
                Box(Modifier.weight(1f)) {
                    HealthInputField("Systolic BP", systolicBP, { systolicBP = it }, keyboardType = KeyboardType.Number)
                }
                Spacer(Modifier.width(12.dp))
                Box(Modifier.weight(1f)) {
                    HealthInputField("Diastolic BP", diastolicBP, { diastolicBP = it }, keyboardType = KeyboardType.Number)
                }
            }

            HealthInputField("Blood Sugar Level", bloodSugarLevel, { bloodSugarLevel = it }, keyboardType = KeyboardType.Number)

            Row(Modifier.fillMaxWidth()) {
                Box(Modifier.weight(1f)) {
                    HealthInputField("Weight (Kg)", weight, { weight = it }, keyboardType = KeyboardType.Number)
                }
                Spacer(Modifier.width(12.dp))
                Box(Modifier.weight(1f)) {
                    HealthInputField("Height (m)", height, { height = it }, keyboardType = KeyboardType.Number)
                }
            }

            HorizontalDivider(
                Modifier.padding(vertical = 24.dp), 
                color = MaterialTheme.colorScheme.outlineVariant
            )
            
            Text(
                text = "Additional Information", 
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold, 
                modifier = Modifier.align(Alignment.Start).padding(vertical = 8.dp), 
                color = HealthBlueDark
            )

            HealthInputField("Gender", gender, { gender = it })
            HealthInputField("Medical Conditions", healthCondition, { healthCondition = it })
            HealthInputField("Emergency Phone", emergencyPhoneNumber, { emergencyPhoneNumber = it }, keyboardType = KeyboardType.Phone)
            HealthInputField("Address", address, { address = it })
            HealthInputField("Allergies", allergies, { allergies = it })
            HealthInputField("Medications", medications, { medications = it })

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage, 
                    color = MaterialTheme.colorScheme.error, 
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (medicalID.isBlank() || name.isBlank()) {
                        errorMessage = "Please enter Medical ID and Name."
                    } else {
                        errorMessage = ""
                        isSaving = true
                        val userHealthData = UserHealthData(
                            medicalID = medicalID,
                            name = name,
                            bloodGroup = bloodGroup,
                            age = age.toIntOrNull() ?: 0,
                            systolicBP = systolicBP.toIntOrNull() ?: 0,
                            diastolicBP = diastolicBP.toIntOrNull() ?: 0,
                            bloodSugarLevel = bloodSugarLevel.toIntOrNull() ?: 0,
                            gender = gender,
                            healthCondition = healthCondition,
                            emergencyPhoneNumber = emergencyPhoneNumber,
                            address = address,
                            allergies = allergies,
                            medications = medications,
                            weight = weight.toFloatOrNull() ?: 0.0f,
                            height = height.toFloatOrNull() ?: 0.0f,
                            timestamp = Timestamp.now()
                        )
                        healthViewModel.saveHealthData(userHealthData, context)
                        isSaving = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp, max = 56.dp)
                    .background(gradient, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent,
                    contentColor = Color.White),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("SAVE HEALTH DATA", fontSize = 16.sp, fontWeight = FontWeight.Bold,
                        )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun HealthInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.padding(top = 12.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(label) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = HealthBlue,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                cursorColor = HealthBlue,
                focusedLabelColor = HealthBlue,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HealthInfoPreview() {
//    HealthInfo(navController = rememberNavController(), healthViewModel = HealthViewModel(
//        HealthRepositoryImpl(Firebase.firestore, Firebase.auth)))
//}
