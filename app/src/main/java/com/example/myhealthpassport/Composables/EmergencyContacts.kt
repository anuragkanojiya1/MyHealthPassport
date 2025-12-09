package com.example.myhealthpassport.Composables

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myhealthpassport.data.database.EmergencyContactDao
import com.example.myhealthpassport.data.database.EmergencyContactDatabase
import com.example.myhealthpassport.data.database.EmergencyContactRepository
import com.example.myhealthpassport.data.database.EmergencyContactViewModel
import com.example.myhealthpassport.data.database.EmergencyContactViewModelFactory
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactsList(contacts: List<EmergencyContact>) {
    val context = LocalContext.current

    var phone by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    val gradient2 = Brush.horizontalGradient(
        colors = listOf(Color(0xFF81D4FA), Color(0xFFB3E5FC)) // Softer gradient
    )

    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF00BCD4), Color(0xFF1E88E5))
    )

    val outlinedFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedTextColor = Color.DarkGray,
        focusedTextColor = Color(0xFF181411),

        cursorColor = Color(0xFF1E88E5) // Blue for cursor
    )

    val rowModifier = Modifier
        .padding(top = 12.dp)
        .background(Color(0xFFB2EBF2).copy(alpha = 0.3f), shape = RoundedCornerShape(12.dp))

    val db = EmergencyContactDatabase.getDatabase(context)
    val dao = db.contactDao()
    val repository = remember { EmergencyContactRepository(dao) }

    val viewModel: EmergencyContactViewModel = viewModel(
        factory = EmergencyContactViewModelFactory(repository)
    )
    val emergencyContacts by viewModel.contacts.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.BottomCenter
    ) {
    Column(Modifier.background(color = Color.White
    ).padding(0.dp, top = 16.dp).fillMaxSize()) {

        Text(text = "Emergency Contact List",
            fontSize = 24.sp,
            fontFamily = FontFamily.Serif,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(4.dp, 4.dp),
            fontWeight = FontWeight.W500
            )

        LazyColumn {
            items(emergencyContacts) { contact ->
                Text(emergencyContacts.toString())
            }
        }

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
                            color = Color(0xFF0288D1), //softer blue
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .animateContentSize(), // Animate size changes
        verticalAlignment = Alignment.Bottom,
    ) {
        var check by remember { mutableStateOf(false) }

        Box(modifier = Modifier.padding(horizontal = 4.dp)) {
            IconButton(
                onClick = {
                    check = !check
                },
                modifier = Modifier
                    .background(brush = gradient, shape = RoundedCornerShape(100))
                    .shadow(elevation = 36.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add, contentDescription = "",
                    modifier = Modifier.clip(shape = RoundedCornerShape(100))
                        .background(color = Color.Transparent, shape = RoundedCornerShape(100)),
                    tint = Color.White
                )
            }
        }

        // Animate visibility of the TextField and Floating Action Button
        AnimatedVisibility(visible = check) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .padding(end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
//                    OutlinedTextField(
//                        value = name,
//                        onValueChange = { newValue -> name = newValue },
//                        label = { Text("Add Emergency Name", color = Color.Gray) },
//                        modifier = Modifier
//                            .padding(end = 8.dp),
//                        singleLine = true,
//                        colors = TextFieldDefaults.outlinedTextFieldColors(
//                            focusedBorderColor = Color.Blue,
//                            unfocusedBorderColor = Color.Gray,
//                            cursorColor = Color.Blue,
//                            focusedTextColor = Color.Black,
//                            focusedPlaceholderColor = Color.Gray,
//                            errorTextColor = Color.Red
//                        ),
//                    )
                    Row(modifier = rowModifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent),
                            value = name,
                            onValueChange = { newValue -> name = newValue },
                            label = {
                                Text(text = "Add Emergency Name", color = Color.DarkGray)
                            },
                            colors = outlinedFieldColors,
                            textStyle = TextStyle(fontSize = 18.sp),
                        )
                    }

//                    OutlinedTextField(
//                        value = phone,
//                        onValueChange = { newValue -> phone = newValue },
//                        label = { Text("Add Emergency Contact", color = Color.Gray) },
//                        modifier = Modifier
//                            .padding(end = 8.dp),
//                        singleLine = true,
//                        colors = TextFieldDefaults.outlinedTextFieldColors(
//                            focusedBorderColor = Color.Blue,
//                            unfocusedBorderColor = Color.Gray,
//                            cursorColor = Color.Blue,
//                            focusedTextColor = Color.Black,
//                            focusedPlaceholderColor = Color.Gray,
//                            errorTextColor = Color.Red
//                        ),
//                    )

                    Row(modifier = rowModifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent),
                            value = phone,
                            onValueChange = { newValue -> phone = newValue },
                            label = {
                                Text(text = "Add Emergency Name", color = Color.DarkGray)
                            },
                            colors = outlinedFieldColors,

                            textStyle = TextStyle(fontSize = 18.sp),
                        )
                    }
                }

                // Button to trigger the query
                ExtendedFloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.addContact(name, phone)
                        }
                    },
                    containerColor = Color(0xFFE9EFF9),
                    modifier = Modifier
                        .background(brush = gradient, shape = RoundedCornerShape(8.dp))
                ) {
                    Text("Add", color = Color.Black)
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

