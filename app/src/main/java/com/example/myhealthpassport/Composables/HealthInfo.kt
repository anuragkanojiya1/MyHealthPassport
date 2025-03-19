package com.example.myhealthpassport.Composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthInfo(navController: NavController, healthViewModel: HealthViewModel){
    var medicalID: String by remember { mutableStateOf("") }
    var name: String by remember { mutableStateOf("") }
    var bloodGroup: String by remember { mutableStateOf("") }
    var age: String by remember { mutableStateOf("") }
    var ageInt: Int by remember { mutableStateOf(0) }

    var bloodPressure by remember { mutableStateOf("") }
    var systolicBP by remember { mutableStateOf("") }
    var systolicBPInt by remember { mutableStateOf(0) }

    var diastolicBP by remember { mutableStateOf("") }
    var diastolicBPInt by remember { mutableStateOf(0) }

    var errorMessage by remember { mutableStateOf("") }

    var bloodSugarLevel: String by remember { mutableStateOf("") }
    var bloodSugarLevelInt: Int by remember { mutableStateOf(0) }
    var weight: String by remember { mutableStateOf("") }
    var weightFloat: Float by remember { mutableStateOf(0.0f) }
    var height: String by remember { mutableStateOf("") }
    var heightFloat: Float by remember { mutableStateOf(0.0f) }
    var gender: String by remember { mutableStateOf("") }
    var healthCondition: String by remember { mutableStateOf("") }
    var emergencyPhoneNumber: String by remember { mutableStateOf("") }
    var emergencyPhoneNumberLong by remember { mutableStateOf(0L) }

    var address: String by remember { mutableStateOf("") }
    var allergies: String by remember { mutableStateOf("") }
    var medications: String by remember { mutableStateOf("") }

    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF00BCD4), Color(0xFF1E88E5))
    )

    val outlinedFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        focusedTextColor = Color(0xFF181411),
        cursorColor = Color(0xFF1E88E5) // Blue for cursor
    )

    val rowModifier = Modifier
        .padding(top = 12.dp)
        .background(Color(0xFFB2EBF2).copy(alpha = 0.3f), shape = RoundedCornerShape(12.dp))

    val context = LocalContext.current
    val scrollView = rememberScrollState()
    val backgroundPainter: Painter = painterResource(id = R.drawable.healthcare)

    Box(modifier = Modifier.fillMaxSize().background(color = Color.White)) {
            Image(
                painter = backgroundPainter,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.matchParentSize()
                    .alpha(0.5f),
            )
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 0.dp)
                .verticalScroll(scrollView)
                .fillMaxWidth()
                .background(color = Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Text(
                text = "Add/Update Medical ID", fontSize = 24.sp, fontStyle = FontStyle.Normal,
                modifier = Modifier
                    .padding(vertical = 12.dp),
                color = Color.Black,
                fontWeight = FontWeight.W500
            )

//            Surface(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .paddingFromBaseline(top = 10.dp, bottom = 10.dp),
//                color = Color.White
//            ) {
////                Box {
////                    AnimatedPreloaderPatient(
////                        modifier = Modifier
////                            .size(300.dp, 200.dp)
////                            .align(Alignment.Center)
////                            .scale(scaleX = 1.3f, scaleY = 1.6f)
////                    )
////                }
//            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Medical ID",
                fontWeight = FontWeight.W500,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp
            )
           Row(modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    value = medicalID,
                    onValueChange = { medicalID = it },
                    label = {
                        Text(text = "Medical ID", color = Color.Gray)
                    },
                   colors = outlinedFieldColors,

                    textStyle = TextStyle(fontSize = 18.sp),
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Name",
                fontWeight = FontWeight.W500,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp
            )
           Row(modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    value = name,
                    onValueChange = { name = it },
                    label = {
                        Text(text = "Name",
                            color = Color.Gray)
                    },
                   colors = outlinedFieldColors,

                    textStyle = TextStyle(fontSize = 18.sp),
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Blood Group",
                fontWeight = FontWeight.W500,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp
            )
           Row(modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    value = bloodGroup,
                    onValueChange = { bloodGroup = it },
                    label = {
                        Text(text = "Blood Group",
                            color = Color.Gray)
                    },
                   colors = outlinedFieldColors,

                    textStyle = TextStyle(fontSize = 18.sp),
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Age",
                fontWeight = FontWeight.W500,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp
            )
           Row(modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    value = age,
                    onValueChange = {
                        age = it
                        if (age.isNotEmpty()) {
                            ageInt = age.toInt()
                        }
                    },
                    label = {
                        Text(text = "Age", color = Color.Gray)
                    },
                   colors = outlinedFieldColors,

                    textStyle = TextStyle(fontSize = 18.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Spacer(Modifier.height(8.dp))

            Row(Modifier.fillMaxWidth()) {
                Column(
                    Modifier
                        .fillMaxWidth(0.5f)
                        .padding(end = 4.dp)
                ) {
                    Text(
                        text = "Systolic BP",
                        fontWeight = FontWeight.W500,
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                    Row(
                        modifier = Modifier
                            .padding(top = 12.dp)
                .background(Color(0xFFB2EBF2).copy(alpha = 0.3f), shape = RoundedCornerShape(12.dp)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent),
                            value = systolicBP,
                            onValueChange = {
                                systolicBP = it
                                if (systolicBP.isNotEmpty()) {
                                    systolicBPInt = systolicBP.toInt()
                                }
                            },
                            label = { Text("Systolic (mmHg)", color = Color.Gray) },
                            isError = errorMessage.isNotEmpty(),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                focusedTextColor = Color(0xFF181411)
                            ),
                            textStyle = TextStyle(fontSize = 18.sp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    }
                }

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                ) {
                    Text(
                        text = "Diastolic BP",
                        fontWeight = FontWeight.W500,
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                    Row(
                        modifier = Modifier
                            .padding(top = 12.dp)
                .background(Color(0xFFB2EBF2).copy(alpha = 0.3f), shape = RoundedCornerShape(12.dp)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent),
                            value = diastolicBP,
                            onValueChange = {
                                diastolicBP = it
                                if (diastolicBP.isNotEmpty()) {
                                    diastolicBPInt = diastolicBP.toInt()
                                }
                            },
                            label = { Text("Diastolic (mmHg)", color = Color.Gray) },
                            isError = errorMessage.isNotEmpty(),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                focusedTextColor = Color(0xFF181411)
                            ),
                            textStyle = TextStyle(fontSize = 18.sp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    }
                }
            }

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(top = 4.dp))
            }


            Spacer(Modifier.height(8.dp))

            Text(
                text = "Blood Sugar",
                fontWeight = FontWeight.W500,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp
            )
           Row(modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    value = bloodSugarLevel,
                    onValueChange = {
                        bloodSugarLevel = it
                        if (bloodSugarLevel.isNotEmpty()) {
                            bloodSugarLevelInt = bloodSugarLevel.toInt()
                        }
                    },
                    label = {
                        Text(text = "Blood Sugar", color = Color.Gray)
                    },
                   colors = outlinedFieldColors,

                    textStyle = TextStyle(fontSize = 18.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Spacer(Modifier.height(8.dp))

            Row(Modifier.fillMaxWidth()) {
                Column(Modifier.fillMaxWidth(0.5f).padding(end = 4.dp)){
                Text(
                    text = "Weight",
                    fontWeight = FontWeight.W500,
                    color = Color.Black,
                    modifier = Modifier,
                    fontSize = 16.sp
                )
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
            .background(Color(0xFFB2EBF2).copy(alpha = 0.3f), shape = RoundedCornerShape(12.dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .background(Color.Transparent),
                        value = weight,
                        onValueChange = {
                            weight = it
                            if (weight.isNotEmpty()) {
                                weightFloat = weight.toFloat()
                            }
                        },
                        label = {
                            Text(text = "Weight", color = Color.Gray)
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color(0xFF181411)
                        ),
                        textStyle = TextStyle(fontSize = 18.sp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
                Column(Modifier.fillMaxWidth().padding(start = 4.dp)) {
                    Text(
                        text = "Height",
                        fontWeight = FontWeight.W500,
                        color = Color.Black,
                        modifier = Modifier,
                        fontSize = 16.sp
                    )
                    Row(
                        modifier = Modifier
                            .padding(top = 12.dp)
                .background(Color(0xFFB2EBF2).copy(alpha = 0.3f), shape = RoundedCornerShape(12.dp)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .background(Color.Transparent),
                            value = height,
                            onValueChange = {
                                height = it
                                if (height.isNotEmpty()) {
                                    heightFloat = height.toFloat()
                                }
                            },
                            label = {
                                Text(text = "Height", color = Color.Gray)
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                focusedTextColor = Color(0xFF181411)
                            ),
                            textStyle = TextStyle(fontSize = 18.sp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Gender",
                fontWeight = FontWeight.W500,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp
            )
           Row(modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    value = gender,
                    onValueChange = { gender = it },
                    label = {
                        Text(text = "Gender", color = Color.Gray)
                    },
                   colors = outlinedFieldColors,

                    textStyle = TextStyle(fontSize = 18.sp),
                )
            }
            Spacer(Modifier.height(8.dp))

            Text(
                text = "Health Condition",
                fontWeight = FontWeight.W500,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp
            )
           Row(modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    value = healthCondition,
                    onValueChange = { healthCondition = it },
                    label = {
                        Text(text = "Health Condition", color = Color.Gray)
                    },
                   colors = outlinedFieldColors,

                    textStyle = TextStyle(fontSize = 18.sp),
                )
            }
            Spacer(Modifier.height(8.dp))

            Text(
                text = "Emergency Phone Number",
                fontWeight = FontWeight.W500,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp
            )
           Row(modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth().background(Color.Transparent),
                    value = emergencyPhoneNumber,
                    onValueChange = {
                        emergencyPhoneNumber = it
                        if (emergencyPhoneNumber.isNotEmpty()) {
                            emergencyPhoneNumberLong = emergencyPhoneNumber.toLong()
                        }
                    },
                    label = { Text("Emergency Phone Number", color = Color.Gray) },
                    isError = errorMessage.isNotEmpty(),
                   colors = outlinedFieldColors,

                    textStyle = TextStyle(fontSize = 18.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

            }
            Spacer(Modifier.height(8.dp))

            Text(
                text = "Address",
                fontWeight = FontWeight.W500,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp
            )
           Row(modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth().background(Color.Transparent),
                    value = address,
                    onValueChange = { address = it },
                    label = {
                        Text(text = "Address", color = Color.Gray)
                    },
                   colors = outlinedFieldColors,

                    textStyle = TextStyle(fontSize = 18.sp),
                )
            }
            Spacer(Modifier.height(8.dp))

            Text(
                text = "Allergies",
                fontWeight = FontWeight.W500,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp
            )
           Row(modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth().background(Color.Transparent),
                    value = allergies,
                    onValueChange = { allergies = it },
                    label = {
                        Text(text = "Allergies", color = Color.Gray)
                    },
                   colors = outlinedFieldColors,

                    textStyle = TextStyle(fontSize = 18.sp),
                )
            }
            Spacer(Modifier.height(8.dp))

            Text(
                text = "Medications",
                fontWeight = FontWeight.W500,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 16.sp
            )
           Row(modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    value = medications,
                    onValueChange = { medications = it },
                    label = {
                        Text(text = "Medications", color = Color.Gray)
                    },
                   colors = outlinedFieldColors,

                    textStyle = TextStyle(fontSize = 18.sp),
                )
            }

            ExtendedFloatingActionButton(
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 16.dp)
                    .background(brush = gradient, shape = RoundedCornerShape(8.dp)),
                containerColor = Color(0xFFE9EFF9),
                onClick = {
                    val userHealthData = UserHealthData(
                        medicalID = medicalID,
                        name = name,
                        bloodGroup = bloodGroup,
                        age = ageInt,
                        systolicBP = systolicBPInt,
                        diastolicBP = diastolicBPInt,
                        bloodSugarLevel = bloodSugarLevel.toInt(),
                        gender = gender,
                        healthCondition = healthCondition,
                        emergencyPhoneNumber = emergencyPhoneNumberLong.toString(),
                        address = address,
                        allergies = allergies,
                        medications = medications,
                        weight = weightFloat,
                        height = heightFloat
                    )
                    healthViewModel.saveHealthData(
                        userHealthData = userHealthData,
                        context = context
                    )
                }) {
                Text(text = "Save Health Data", color = Color.Black)
            }
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