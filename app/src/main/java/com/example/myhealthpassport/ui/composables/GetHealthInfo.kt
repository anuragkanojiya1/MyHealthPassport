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
import com.example.myhealthpassport.viewmodels.HealthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetHealthInfo(navController: NavController, healthViewModel: HealthViewModel) {
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

    val context = LocalContext.current
    val scrollView = rememberScrollState()
    val backgroundPainter: Painter = painterResource(id = R.drawable.healthcare)

    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF00BCD4), Color(0xFF1E88E5))
    )

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
                    text = "Find Health Record",
                    fontSize = 22.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                Box(modifier = Modifier.weight(1f)) {
                    HealthInputField("Medical ID to Search", medicalID, { medicalID = it })
                }
                Spacer(modifier = Modifier.width(8.dp))
                
                ExtendedFloatingActionButton(
                    onClick = {
                        healthViewModel.retrieveHealthData(medicalID, context) { data ->
                            name = data.name
                            bloodGroup = data.bloodGroup
                            age = data.age.toString()
                            systolicBP = data.systolicBP.toString()
                            diastolicBP = data.diastolicBP.toString()
                            bloodSugarLevel = data.bloodSugarLevel.toString()
                            weight = data.weight.toString()
                            height = data.height.toString()
                            gender = data.gender
                            healthCondition = data.healthCondition
                            emergencyPhoneNumber = data.emergencyPhoneNumber
                            address = data.address
                            allergies = data.allergies
                            medications = data.medications
                        }
                    },
                    modifier = Modifier
                        .height(54.dp)
                        .background(brush = gradient, shape = RoundedCornerShape(12.dp)),
                    containerColor = Color.Transparent,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp)
                ) {
                    Text("GET DATA", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            HorizontalDivider(Modifier.padding(vertical = 16.dp), thickness = 0.5.dp, color = Color.LightGray)

            HealthInputField("Patient Name", name, { name = it })
            
            Row(Modifier.fillMaxWidth()) {
                Box(Modifier.weight(1f)) {
                    HealthInputField("Blood Group", bloodGroup, { bloodGroup = it })
                }
                Spacer(Modifier.width(12.dp))
                Box(Modifier.weight(1f)) {
                    HealthInputField("Age", age, { age = it }, keyboardType = KeyboardType.Number)
                }
            }

            HorizontalDivider(Modifier.padding(vertical = 16.dp), thickness = 0.5.dp, color = Color.LightGray)
            Text("Vitals", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.align(Alignment.Start), color = Color(0xFF1E88E5))

            Row(Modifier.fillMaxWidth()) {
                Box(Modifier.weight(1f)) {
                    HealthInputField("Systolic BP", systolicBP, { systolicBP = it }, keyboardType = KeyboardType.Number)
                }
                Spacer(Modifier.width(12.dp))
                Box(Modifier.weight(1f)) {
                    HealthInputField("Diastolic BP", diastolicBP, { diastolicBP = it }, keyboardType = KeyboardType.Number)
                }
            }

            HealthInputField("Blood Sugar", bloodSugarLevel, { bloodSugarLevel = it }, keyboardType = KeyboardType.Number)

            Row(Modifier.fillMaxWidth()) {
                Box(Modifier.weight(1f)) {
                    HealthInputField("Weight", weight, { weight = it }, keyboardType = KeyboardType.Number)
                }
                Spacer(Modifier.width(12.dp))
                Box(Modifier.weight(1f)) {
                    HealthInputField("Height", height, { height = it }, keyboardType = KeyboardType.Number)
                }
            }

            HorizontalDivider(Modifier.padding(vertical = 16.dp), thickness = 0.5.dp, color = Color.LightGray)
            
            HealthInputField("Gender", gender, { gender = it })
            HealthInputField("Medical Conditions", healthCondition, { healthCondition = it })
            HealthInputField("Emergency Contact", emergencyPhoneNumber, { emergencyPhoneNumber = it }, keyboardType = KeyboardType.Number)
            HealthInputField("Address", address, { address = it })
            HealthInputField("Allergies", allergies, { allergies = it })
            HealthInputField("Medications", medications, { medications = it })

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(top = 12.dp), fontSize = 14.sp)
            }

            Column(modifier = Modifier.padding(vertical = 32.dp).fillMaxWidth()) {
                
                ExtendedFloatingActionButton(
                    onClick = {
                        val patientData = listOf(
                            medicalID, name, bloodGroup, age, gender,
                            healthCondition, emergencyPhoneNumber, address,
                            allergies, medications
                        )
                        navController.navigate("patient_details/$patientData")
                    },
                    modifier = Modifier.fillMaxWidth().height(54.dp).background(brush = gradient, shape = RoundedCornerShape(12.dp)),
                    containerColor = Color.Transparent,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp)
                ) {
                    Text("VIEW DETAILED SUMMARY", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                ExtendedFloatingActionButton(
                    onClick = {
                        healthViewModel.delete(medicalID, context, navController)
                    },
                    modifier = Modifier.fillMaxWidth().height(54.dp).background(
                        brush = Brush.horizontalGradient(listOf(Color(0xFFFF5252), Color(0xFFFF1744))), 
                        shape = RoundedCornerShape(12.dp)
                    ),
                    containerColor = Color.Transparent,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp)
                ) {
                    Text("DELETE RECORD", color = Color.White, fontWeight = FontWeight.Bold)
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
fun GetHealthInfoPreview() {
    // Preview
}
