package com.example.myhealthpassport.Composables

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.random.Random

@Composable
fun EmergencyContactsList(contacts: List<EmergencyContact>) {
    val context = LocalContext.current

    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF81D4FA), Color(0xFFB3E5FC)) // Softer gradient
    )

    val gradient2 = Brush.horizontalGradient(
        colors = listOf(Color(0xFF44A6FC), Color(0xFF75F8F2))
    )

    Column(Modifier.background(color = Color.White).padding(0.dp, top = 16.dp).fillMaxSize()) {

        Text(text = "Emergency Contact List",
            fontSize = 24.sp,
            fontFamily = FontFamily.Serif,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(4.dp, 4.dp),
            fontWeight = FontWeight.W500
            )

        LazyColumn(modifier = Modifier.padding(top = 24.dp).fillMaxSize()) {

            items(contacts) { contact ->
                Row(modifier = Modifier
                    .padding(
                        horizontal = 16.dp,
                    vertical = 16.dp)
                    .border(1.dp,  Color(0xFF1E88E5), shape = RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .background(Color(0xFFB2EBF2), RoundedCornerShape(12.dp)) // Lighter but visible cyan
                    .clip(RoundedCornerShape(12.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Card(shape = CardDefaults.outlinedShape,
                        modifier = Modifier
                            .size(width = 44.dp, height = 40.dp)
                            .padding(horizontal = 4.dp)
                            .align(Alignment.CenterVertically)
                            .border(1.dp, Color(0xFF1E88E5), shape = CardDefaults.outlinedShape),
                        colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Call,
                            contentDescription = "",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                .padding(vertical = 8.dp),
                            tint = Color(0xFF1E88E5),
                            )
                    }
                    Column(modifier = Modifier.fillMaxWidth(0.7f).padding(start = 16.dp)){
                        Text(text = contact.phoneNumber,
                            color = Color.Black,
                                    fontSize = 20.sp)
                        Text(text = contact.name,
                            color = Color(0xFF0288D1), // Softer blue
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.Start))
                    }
//                    Text(
//                        text = contact.name,
//                        fontSize = 20.sp,
//                        style = MaterialTheme.typography.bodyLarge,
//                        modifier = Modifier
//                            .background(color = getRandomColor())
//                            .fillMaxWidth()
//                            .padding(8.dp, 8.dp),
//                        color = Color.Black,
//                        textAlign = TextAlign.Center
//                    )
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${contact.phoneNumber}")
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .padding(4.dp, 4.dp),
                        border = _root_ide_package_.androidx.compose.foundation.BorderStroke(1.dp,Color(0xFF1E88E5)
                        )
                    ) {
                        Text(text = "Call",
                            fontSize = 18.sp,
                            color = Color.White,
                            fontFamily = FontFamily.SansSerif)
                    }
                }
            }
        }
    }
}

@Composable
fun EmergencyContactsListPreview(navController: NavController) {
    val contacts = listOf(
        EmergencyContact("Police", "100"),
        EmergencyContact("Ambulance", "102"),
        EmergencyContact("Fire Department", "101"),
        EmergencyContact("Poison Control", "1066"),
        EmergencyContact("Electricity Emergency", "115"),
        EmergencyContact("Women Helpline", "1091"),
        EmergencyContact("Disaster Management ( N.D.M.A )", "1078"),
        EmergencyContact("Senior Citizen Helpline", "14567"),
        EmergencyContact("Railway Accident Emergency Service", "1072"),
        EmergencyContact("Road Accident Emergency Service", "1912"),
    )

    MaterialTheme {
        EmergencyContactsList(contacts = contacts)
    }
}

@Preview(showBackground = true)
@Composable
fun EmergencyContactsListPrevie() {
    val contacts = listOf(
        EmergencyContact("Police", "100"),
        EmergencyContact("Ambulance", "102"),
        EmergencyContact("Fire Department", "101"),
        EmergencyContact("Poison Control", "1066"),
        EmergencyContact("Electricity Emergency", "115"),
        EmergencyContact("Women Helpline", "1091"),
        EmergencyContact("Disaster Management ( N.D.M.A )", "1078"),
        EmergencyContact("Senior Citizen Helpline", "14567"),
        EmergencyContact("Railway Accident Emergency Service", "1072"),
        EmergencyContact("Road Accident Emergency Service", "1912"),
    )

    MaterialTheme {
        EmergencyContactsList(contacts = contacts)
    }
}

@Composable
fun getRandomColor(): Color {
    return Color(
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat(),
        alpha = 0.5f
    )
}

