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
import com.example.myhealthpassport.domain.model.HealthInfoState
import com.example.myhealthpassport.ui.theme.HealthBlue
import com.example.myhealthpassport.ui.theme.HealthBlueDark
import com.example.myhealthpassport.viewmodels.HealthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetHealthInfo(navController: NavController, healthViewModel: HealthViewModel) {
//    var medicalID by remember { mutableStateOf("") }
//    var name by remember { mutableStateOf("") }
//    var bloodGroup by remember { mutableStateOf("") }
//    var age by remember { mutableStateOf("") }
//    var systolicBP by remember { mutableStateOf("") }
//    var diastolicBP by remember { mutableStateOf("") }
//    var bloodSugarLevel by remember { mutableStateOf("") }
//    var weight by remember { mutableStateOf("") }
//    var height by remember { mutableStateOf("") }
//    var gender by remember { mutableStateOf("") }
//    var healthCondition by remember { mutableStateOf("") }
//    var emergencyPhoneNumber by remember { mutableStateOf("") }
//    var address by remember { mutableStateOf("") }
//    var allergies by remember { mutableStateOf("") }
//    var medications by remember { mutableStateOf("") }
//
//    var errorMessage by remember { mutableStateOf("") }

    var state by remember {
        mutableStateOf(HealthInfoState())
    }

    val context = LocalContext.current
    val scrollView = rememberScrollState()
    val backgroundPainter: Painter = painterResource(id = R.drawable.healthcare)

    val gradient = Brush.horizontalGradient(
        colors = listOf(HealthBlue, HealthBlueDark)
    )

    Box(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background)) {
        Image(
            painter = backgroundPainter,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.matchParentSize().alpha(0.35f),
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollView)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            
//            Row(
//                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                IconButton(onClick = { navController.popBackStack() }) {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                        contentDescription = "Back",
//                        tint = MaterialTheme.colorScheme.onBackground
//                    )
//                }
//                Text(
//                    text = "Find Health Record",
//                    fontSize = 22.sp,
//                    color = MaterialTheme.colorScheme.onBackground,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.padding(start = 8.dp)
//                )
//            }

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                Box(modifier = Modifier.weight(1f)) {
                    HealthInputField("Medical ID to Search", state.medicalID, { state = state.copy(medicalID = it) })
                }
                Spacer(modifier = Modifier.width(8.dp))
                
                ExtendedFloatingActionButton(
                    onClick = {
                        if (state.medicalID.isNotBlank()) {
                            healthViewModel.retrieveHealthData(state.medicalID, context) { data ->
                                state = state.copy(
                                name = data.name,
                                bloodGroup = data.bloodGroup,
                                age = data.age.toString(),
                                systolicBP = data.systolicBP.toString(),
                                diastolicBP = data.diastolicBP.toString(),
                                bloodSugarLevel = data.bloodSugarLevel.toString(),
                                weight = data.weight.toString(),
                                height = data.height.toString(),
                                gender = data.gender,
                                healthCondition = data.healthCondition,
                                emergencyPhoneNumber = data.emergencyPhoneNumber,
                                address = data.address,
                                allergies = data.allergies,
                                medications = data.medications
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .heightIn(min = 48.dp, max = 56.dp)
                        .background(brush = gradient, shape = RoundedCornerShape(12.dp)),
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp)
                ) {
                    Text("GET DATA", fontWeight = FontWeight.Bold)
                }
            }

            HorizontalDivider(
                Modifier.padding(vertical = 24.dp),
                thickness = 0.5.dp, 
                color = MaterialTheme.colorScheme.outlineVariant
            )

            HealthInputField("Patient Name", state.name, { state = state.copy(name = it) })
            
            Row(Modifier.fillMaxWidth()) {
                Box(Modifier.weight(1f)) {
                    HealthInputField("Blood Group", state.bloodGroup, { state = state.copy(bloodGroup = it) })
                }
                Spacer(Modifier.width(12.dp))
                Box(Modifier.weight(1f)) {
                    HealthInputField("Age", state.age, { state = state.copy(age = it) }, keyboardType = KeyboardType.Number)
                }
            }

            HorizontalDivider(
                Modifier.padding(vertical = 24.dp), 
                thickness = 0.5.dp, 
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
                    HealthInputField("Systolic BP", state.systolicBP, { state = state.copy(systolicBP = it) }, keyboardType = KeyboardType.Number)
                }
                Spacer(Modifier.width(12.dp))
                Box(Modifier.weight(1f)) {
                    HealthInputField("Diastolic BP", state.diastolicBP, { state = state.copy(diastolicBP = it) }, keyboardType = KeyboardType.Number)
                }
            }

            HealthInputField("Blood Sugar", state.bloodSugarLevel, { state = state.copy(bloodSugarLevel = it) }, keyboardType = KeyboardType.Number)

            Row(Modifier.fillMaxWidth()) {
                Box(Modifier.weight(1f)) {
                    HealthInputField("Weight", state.weight, { state = state.copy(weight = it) }, keyboardType = KeyboardType.Number)
                }
                Spacer(Modifier.width(12.dp))
                Box(Modifier.weight(1f)) {
                    HealthInputField("Height", state.height, { state = state.copy(height = it) }, keyboardType = KeyboardType.Number)
                }
            }

            HorizontalDivider(
                Modifier.padding(vertical = 24.dp), 
                thickness = 0.5.dp, 
                color = MaterialTheme.colorScheme.outlineVariant
            )
            
            HealthInputField("Gender", state.gender, { state = state.copy(gender = it) })
            HealthInputField("Medical Conditions", state.healthCondition, { state = state.copy(healthCondition = it) })
            HealthInputField("Emergency Contact", state.emergencyPhoneNumber, { state = state.copy(emergencyPhoneNumber = it) }, keyboardType = KeyboardType.Number)
            HealthInputField("Address", state.address, { state = state.copy(address = it) })
            HealthInputField("Allergies", state.allergies, { state = state.copy(allergies = it) })
            HealthInputField("Medications", state.medications, { state = state.copy(medications = it) })

            state.errorMessage?.let {
                if (it.isNotEmpty()) {
                    Text(
                        text = state.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 12.dp),
                        fontSize = 14.sp
                    )
                }
            }

            Column(modifier = Modifier.padding(vertical = 32.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                
                ExtendedFloatingActionButton(
                    onClick = {
                        navController.navigate("patient_details/${state.medicalID}")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp, max = 54.dp)
                        .background(brush = gradient, shape = RoundedCornerShape(12.dp)),
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp)
                ) {
                    Text("VIEW DETAILED SUMMARY", fontWeight = FontWeight.Bold)
                }

                ExtendedFloatingActionButton(
                    onClick = {
                        if (state.medicalID.isNotBlank()) {
                            healthViewModel.delete(state.medicalID, context, navController)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp, max = 54.dp)
                        .background(
                            brush = Brush.horizontalGradient(listOf(Color(0xFFFF5252), Color(0xFFFF1744))), 
                            shape = RoundedCornerShape(12.dp)
                        ),
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp)
                ) {
                    Text("DELETE RECORD", fontWeight = FontWeight.Bold)
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
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        textStyle = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
    )
}

@Preview(showBackground = true)
@Composable
fun GetHealthInfoPreview() {
    // Preview
}
