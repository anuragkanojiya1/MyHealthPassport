package com.example.myhealthpassport.Composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myhealthpassport.R

@Composable
fun PatientDetails(navController: NavController, patientData: List<String>) {

    val medicalID = patientData[0]
    val name = patientData[1]
    val bloodGroup = patientData[2]
    val age = patientData[3]
    val weight = patientData[4]
    val height = patientData[5]
    val gender = patientData[6]
    val healthCondition = patientData[7]
    val emergencyPhoneNumber = patientData[8]
    val address = patientData[9]
    val allergies = patientData[10]
    val medications = patientData[11]

    val medicaldetails = listOf(
        "Medical ID: $medicalID",
        "Name: $name",
        "Blood Group: $bloodGroup",
        "Age: $age",
        "Weight in Kg: $weight",
        "Height: $height",
        "Gender: $gender",
        "Health Condition: $healthCondition",
        "Emergency Phone Number: $emergencyPhoneNumber",
        "Address: $address",
        "Allergies: $allergies",
        "Medications: $medications"
    )

    val backgroundPainter: Painter = painterResource(id = R.drawable.healthcare)

    Box {
        Image(
            painter = backgroundPainter,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.matchParentSize()
        )
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Patient Details", fontSize = 30.sp,
                fontFamily = FontFamily.Serif,
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .padding(10.dp)
                    .background(color = Color.Transparent)
            ) {
                items(medicaldetails) { data ->
                    Card(
                        modifier = Modifier
                            .padding(10.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(color = Color.Cyan, shape = RoundedCornerShape(8.dp))
                            .border(1.dp, color = Color.Blue, shape = RoundedCornerShape(8.dp))
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                            contentColor = Color.Black,
                            disabledContainerColor = Color.Blue,
                            disabledContentColor = Color.Magenta
                        )
                    ) {
                        Text(
                            text = data,
                            modifier = Modifier.padding(16.dp),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PatientPreview() {
    val sampleData = listOf(
        "123455", "John Doe", "A+", "30","45","1.7", "Male", "Healthy", "1234557590", "123 Main St",
        "NA","NA"
    )
    PatientDetails(navController = rememberNavController(), patientData = sampleData)
}



//@Preview(showBackground = true)
//@Composable
//fun PatientDetailsPreview()
//{
//    val sampleData = listOf(
//        PatientData(
//            medicalID = "123455",
//            name = "John Doe",
//            bloodGroup = "A+",
//            age = 30,
//            gender = "Male",
//            healthCondition = "Healthy",
//            emergencyPhoneNumber = 1234557590,
//            address = "123 Main St",
//            allergies = "None",
//            medications = "None"
//        ),
//        PatientData(
//            medicalID = "554321",
//            name = "Jane Smith",
//            bloodGroup = "O-",
//            age = 25,
//            gender = "Female",
//            healthCondition = "Asthma",
//            emergencyPhoneNumber = 957554720,
//            address = "455 Elm St",
//            allergies = "Peanuts",
//            medications = "Inhaler"
//        ),
//        PatientData(
//            medicalID = "554321",
//            name = "Jane Smith",
//            bloodGroup = "O-",
//            age = 25,
//            gender = "Female",
//            healthCondition = "Asthma",
//            emergencyPhoneNumber = 957554720,
//            address = "455 Elm St",
//            allergies = "Peanuts",
//            medications = "Inhaler"
//        ),
//        PatientData(
//            medicalID = "554321",
//            name = "Jane Smith",
//            bloodGroup = "O-",
//            age = 25,
//            gender = "Female",
//            healthCondition = "Asthma",
//            emergencyPhoneNumber = 957554720,
//            address = "455 Elm St",
//            allergies = "Peanuts",
//            medications = "Inhaler"
//        )
//    )
//    PatientDetails(navController = rememberNavController(), patientData = sampleData)
//}