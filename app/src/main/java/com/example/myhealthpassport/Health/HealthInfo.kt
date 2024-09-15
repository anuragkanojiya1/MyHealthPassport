package com.example.myhealthpassport.Health

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.myhealthpassport.R

import com.example.myhealthpassport.SignInSignUp.AnimatedPreloaderPatient
import com.example.myhealthpassport.ViewModels.HealthViewModel

@Composable
fun HealthInfo(navController: NavController, healthViewModel: HealthViewModel){
    var medicalID: String by remember { mutableStateOf("") }
    var name: String by remember { mutableStateOf("") }
    var bloodGroup: String by remember { mutableStateOf("") }
    var age: String by remember { mutableStateOf("") }
    var ageInt: Int by remember { mutableStateOf(0) }
    var weight: String by remember { mutableStateOf("") }
    var weightFloat: Float by remember { mutableStateOf(0.0f) }
    var height: String by remember { mutableStateOf("") }
    var heightFloat: Float by remember { mutableStateOf(0.0f) }
    var gender: String by remember { mutableStateOf("") }
    var healthCondition: String by remember { mutableStateOf("") }
    var emergencyPhoneNumber: String by remember { mutableStateOf("") }
    var emergencyPhoneNumberLong: Long by remember { mutableStateOf(0) }

    var address: String by remember { mutableStateOf("") }
    var allergies: String by remember { mutableStateOf("") }
    var medications: String by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scrollView = rememberScrollState()

    Column(modifier = Modifier
        .padding(start = 40.dp, end = 40.dp, bottom = 40.dp)
        .verticalScroll(scrollView)
        .fillMaxWidth()
        .background(color = Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "Health Information", fontSize = 30.sp, fontStyle = FontStyle.Normal,
            modifier = Modifier
                .padding(vertical = 15.dp)
        )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 10.dp, bottom = 10.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Box {
                    AnimatedPreloaderPatient(modifier = Modifier
                        .size(300.dp, 200.dp)
                        .align(Alignment.Center)
                        .scale(scaleX = 1.3f, scaleY = 1.6f)
                    )
                }
        }
        OutlinedTextField( modifier = Modifier
            .fillMaxWidth(),
            value = medicalID,
            onValueChange = { medicalID=it },
            label = {
                Text(text = "Medical ID")
            }
        )
    OutlinedTextField( modifier = Modifier
            .fillMaxWidth(),
            value = name,
            onValueChange = { name=it },
            label = {
                Text(text = "Name")
            }
        )
    OutlinedTextField( modifier = Modifier
            .fillMaxWidth(),
            value = bloodGroup,
            onValueChange = { bloodGroup=it },
            label = {
                Text(text = "Blood Group")
            }
        )
        OutlinedTextField( modifier = Modifier
            .fillMaxWidth(),
            value = age,
            onValueChange = {
                age=it
                if(age.isNotEmpty()){
                    ageInt = age.toInt()
                }},
            label = {
                Text(text = "Age")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField( modifier = Modifier
            .fillMaxWidth(),
            value = weight,
            onValueChange = {
                weight=it
                if(weight.isNotEmpty()){
                    weightFloat = weight.toFloat()
                }},
            label = {
                Text(text = "Weight")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField( modifier = Modifier
            .fillMaxWidth(),
            value = height,
            onValueChange = {
                height=it
                if(height.isNotEmpty()){
                    heightFloat = height.toFloat()
                }},
            label = {
                Text(text = "Height")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    OutlinedTextField( modifier = Modifier
            .fillMaxWidth(),
            value = gender,
            onValueChange = { gender=it },
            label = {
                Text(text = "Gender")
            }
        )
    OutlinedTextField( modifier = Modifier
            .fillMaxWidth(),
            value = healthCondition,
            onValueChange = { healthCondition=it },
            label = {
                Text(text = "Health Condition")
            }
        )
    OutlinedTextField( modifier = Modifier
            .fillMaxWidth(),
            value = emergencyPhoneNumber,
            onValueChange = {
                emergencyPhoneNumber=it
                if(emergencyPhoneNumber.isNotEmpty()){
                    emergencyPhoneNumberLong = emergencyPhoneNumber.toLong()
                }},
            label = {
                Text(text = "Emergency Phone Number")
            },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    OutlinedTextField( modifier = Modifier
            .fillMaxWidth(),
            value = address,
            onValueChange = { address=it },
            label = {
                Text(text = "Address")
            }
        )
    OutlinedTextField( modifier = Modifier
            .fillMaxWidth(),
            value = allergies,
            onValueChange = { allergies=it },
            label = {
                Text(text = "Allergies")
            }
        )
    OutlinedTextField( modifier = Modifier
            .fillMaxWidth(),
            value = medications,
            onValueChange = { medications=it },
            label = {
                Text(text = "Medications")
            }
        )

        Button(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth(),
            onClick = {
                val userHealthData = UserHealthData(
                    medicalID = medicalID,
                    name = name,
                    bloodGroup = bloodGroup,
                    age = ageInt,
                    gender = gender,
                    healthCondition = healthCondition,
                    emergencyPhoneNumber = emergencyPhoneNumberLong,
                    address = address,
                    allergies = allergies,
                    medications = medications,
                    weight = weightFloat,
                    height = heightFloat
                )
                healthViewModel.saveHealthData(userHealthData = userHealthData, context = context)
            }) {
                Text(text = "Save Health Data")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HealthInfoPreview(){
    HealthInfo(navController = rememberNavController(), healthViewModel = HealthViewModel())
}

@Composable
fun AnimatedPreloaderDoctor(modifier: Modifier = Modifier) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.lottieanimationdoctor
        )
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )


    LottieAnimation(
        composition = preloaderLottieComposition,
        progress = preloaderProgress,
        modifier = modifier
    )
}

@Composable
fun AnimatedPreloaderMainHealthActivity(modifier: Modifier = Modifier) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.healthactivityanimation
        )
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )


    LottieAnimation(
        composition = preloaderLottieComposition,
        progress = preloaderProgress,
        modifier = modifier
    )
}

//LottieAnimationsTheme {
//    // A surface container using the 'background' color from the theme
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = MaterialTheme.colorScheme.background
//    ) {
//        Box {
//            AnimatedPreloader(modifier = Modifier.size(200.dp).align(Alignment.Center))
//        }
//    }
//}