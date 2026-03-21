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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myhealthpassport.R
import com.example.myhealthpassport.domain.model.UserHealthData
import com.example.myhealthpassport.viewmodels.HealthViewModel
import com.google.firebase.Timestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthInfo(navController: NavController, healthViewModel: HealthViewModel) {
    var medicalID by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var ageInt by remember { mutableIntStateOf(0) }

    var systolicBP by remember { mutableStateOf("") }
    var systolicBPInt by remember { mutableIntStateOf(0) }

    var diastolicBP by remember { mutableStateOf("") }
    var diastolicBPInt by remember { mutableIntStateOf(0) }

    var errorMessage by remember { mutableStateOf("") }

    var bloodSugarLevel by remember { mutableStateOf("") }
    var bloodSugarLevelInt by remember { mutableIntStateOf(0) }
    var weight by remember { mutableStateOf("") }
    var weightFloat by remember { mutableFloatStateOf(0.0f) }
    var height by remember { mutableStateOf("") }
    var heightFloat by remember { mutableFloatStateOf(0.0f) }
    var gender by remember { mutableStateOf("") }
    var healthCondition by remember { mutableStateOf("") }
    var emergencyPhoneNumber by remember { mutableStateOf("") }
    var emergencyPhoneNumberLong by remember { mutableLongStateOf(0L) }

    var address by remember { mutableStateOf("") }
    var allergies by remember { mutableStateOf("") }
    var medications by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scrollView = rememberScrollState()
    val backgroundPainter: Painter = painterResource(id = R.drawable.healthcare)

    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF00BCD4), Color(0xFF1E88E5))
    )

    // Automatically load existing health data if it exists
    LaunchedEffect(Unit) {
        val latestData = healthViewModel.getLatestHealthData(context)
        latestData?.let {
            medicalID = it.medicalID
            name = it.name
            bloodGroup = it.bloodGroup
            age = it.age.toString()
            ageInt = it.age
            systolicBP = it.systolicBP.toString()
            systolicBPInt = it.systolicBP
            diastolicBP = it.diastolicBP.toString()
            diastolicBPInt = it.diastolicBP
            bloodSugarLevel = it.bloodSugarLevel.toString()
            bloodSugarLevelInt = it.bloodSugarLevel
            weight = it.weight.toString()
            weightFloat = it.weight
            height = it.height.toString()
            heightFloat = it.height
            gender = it.gender
            healthCondition = it.healthCondition
            emergencyPhoneNumber = it.emergencyPhoneNumber
            emergencyPhoneNumberLong = it.emergencyPhoneNumber.toLongOrNull() ?: 0L
            address = it.address
            allergies = it.allergies
            medications = it.medications
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(color = Color.White)) {
        Image(
            painter = backgroundPainter,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.matchParentSize().alpha(0.15f),
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollView)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                }
                Text(
                    text = "Update Health Profile",
                    fontSize = 22.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            HealthInputField("Medical ID (Required)", medicalID, { medicalID = it })
            HealthInputField("Full Name", name, { name = it })
            
            Row(Modifier.fillMaxWidth()) {
                Box(Modifier.weight(1f)) {
                    HealthInputField("Blood Group", bloodGroup, { bloodGroup = it })
                }
                Spacer(Modifier.width(12.dp))
                Box(Modifier.weight(1f)) {
                    HealthInputField("Age", age, { 
                        age = it
                        ageInt = it.toIntOrNull() ?: 0 
                    }, keyboardType = KeyboardType.Number)
                }
            }

            HorizontalDivider(Modifier.padding(vertical = 16.dp), thickness = 0.5.dp, color = Color.LightGray)
            Text("Vitals", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.align(Alignment.Start), color = Color(0xFF1E88E5))

            Row(Modifier.fillMaxWidth()) {
                Box(Modifier.weight(1f)) {
                    HealthInputField("Systolic BP", systolicBP, { 
                        systolicBP = it
                        systolicBPInt = it.toIntOrNull() ?: 0 
                    }, keyboardType = KeyboardType.Number)
                }
                Spacer(Modifier.width(12.dp))
                Box(Modifier.weight(1f)) {
                    HealthInputField("Diastolic BP", diastolicBP, { 
                        diastolicBP = it
                        diastolicBPInt = it.toIntOrNull() ?: 0 
                    }, keyboardType = KeyboardType.Number)
                }
            }

            HealthInputField("Blood Sugar", bloodSugarLevel, { 
                bloodSugarLevel = it
                bloodSugarLevelInt = it.toIntOrNull() ?: 0 
            }, keyboardType = KeyboardType.Number)

            Row(Modifier.fillMaxWidth()) {
                Box(Modifier.weight(1f)) {
                    HealthInputField("Weight", weight, { 
                        weight = it
                        weightFloat = it.toFloatOrNull() ?: 0.0f 
                    }, keyboardType = KeyboardType.Number)
                }
                Spacer(Modifier.width(12.dp))
                Box(Modifier.weight(1f)) {
                    HealthInputField("Height", height, { 
                        height = it
                        heightFloat = it.toFloatOrNull() ?: 0.0f 
                    }, keyboardType = KeyboardType.Number)
                }
            }

            HorizontalDivider(Modifier.padding(vertical = 16.dp), thickness = 0.5.dp, color = Color.LightGray)

            HealthInputField("Gender", gender, { gender = it })
            HealthInputField("Medical Conditions", healthCondition, { healthCondition = it })
            HealthInputField("Emergency Phone", emergencyPhoneNumber, { 
                emergencyPhoneNumber = it
                emergencyPhoneNumberLong = it.toLongOrNull() ?: 0L 
            }, keyboardType = KeyboardType.Number)
            HealthInputField("Address", address, { address = it })
            HealthInputField("Allergies", allergies, { allergies = it })
            HealthInputField("Medications", medications, { medications = it })

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(top = 12.dp), fontSize = 14.sp)
            }

            Button(
                onClick = {
                    if (medicalID.isBlank() || name.isBlank()) {
                        errorMessage = "Please enter Medical ID and Name."
                    } else {
                        errorMessage = ""
                        val userHealthData = UserHealthData(
                            medicalID = medicalID,
                            name = name,
                            bloodGroup = bloodGroup,
                            age = ageInt,
                            systolicBP = systolicBPInt,
                            diastolicBP = diastolicBPInt,
                            bloodSugarLevel = bloodSugarLevelInt,
                            gender = gender,
                            healthCondition = healthCondition,
                            emergencyPhoneNumber = emergencyPhoneNumberLong.toString(),
                            address = address,
                            allergies = allergies,
                            medications = medications,
                            weight = weightFloat,
                            height = heightFloat,
                            timestamp = Timestamp.now()
                        )
                        healthViewModel.saveHealthData(userHealthData, context)
                    }
                },
                modifier = Modifier
                    .padding(vertical = 32.dp)
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(brush = gradient, shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("SAVE HEALTH DATA", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
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
        Text(text = label, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Gray)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .background(Color(0xFFB2EBF2).copy(alpha = 0.25f), shape = RoundedCornerShape(12.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00BCD4),
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color(0xFF1E88E5)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            textStyle = TextStyle(fontSize = 16.sp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HealthInfoPreview() {
    // Preview
}
