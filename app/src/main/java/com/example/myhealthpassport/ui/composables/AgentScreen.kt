package com.example.myhealthpassport.ui.composables

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myhealthpassport.R
import com.example.myhealthpassport.data.repository.HealthRepositoryImpl
import com.example.myhealthpassport.ui.components.formatText
import com.example.myhealthpassport.ui.theme.HealthBlue
import com.example.myhealthpassport.ui.theme.HealthBlueDark
import com.example.myhealthpassport.ui.navigation.Screen
import com.example.myhealthpassport.viewmodels.AgentViewModel
import com.example.myhealthpassport.viewmodels.ApiKeyViewModel
import com.example.myhealthpassport.viewmodels.HealthViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentScreen(
    navController: NavController,
    agentViewModel: AgentViewModel,
    healthViewModel: HealthViewModel,
    apiKeyViewModel: ApiKeyViewModel = hiltViewModel()
) {

    val isApiKeyMissing by apiKeyViewModel.isApiKeyMissing.collectAsState()

    // NAVIGATION GUARD: Redirect if key is missing
    LaunchedEffect(isApiKeyMissing) {
        if (isApiKeyMissing == true) {
            navController.navigate(Screen.ApiKeySettings.route) {
                popUpTo(Screen.AgentScreen.route) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    if (isApiKeyMissing != false) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = HealthBlue)
        }
        return
    }

    var query by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val agentResponse by agentViewModel.agentResponse.observeAsState()

    var medicalID by remember { mutableStateOf("") }
    var name: String by remember { mutableStateOf("") }
    var bloodGroup: String by remember { mutableStateOf("") }
    var age: String by remember { mutableStateOf("") }

    var systolicBP by remember { mutableStateOf<Int?>(null) }
    var diastolicBP by remember { mutableStateOf<Int?>(null) }

    var bloodSugarLevel by remember { mutableStateOf("") }
    var gender: String by remember { mutableStateOf("") }
    var healthCondition: String by remember { mutableStateOf("") }
    var allergies: String by remember { mutableStateOf("") }
    var medications: String by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var queryAdd by remember { mutableStateOf("") }

    val gradient = Brush.horizontalGradient(
        colors = listOf(HealthBlue, HealthBlueDark)
    )

    val context = LocalContext.current


        val backgroundPainter: Painter = painterResource(id = R.drawable.healthcare)
        val scrollView = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(vertical = 8.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        ) {
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
                Text(
                    text = "Get your Personalized Diet with Exercise Recommendation Plan by our Agent...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 24.dp),
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = medicalID,
                        onValueChange = { medicalID = it },
                        shape = RoundedCornerShape(12.dp),
                        label = {
                            Text(text = "MedicalID")
                        },
                        colors = textFieldColors(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    ExtendedFloatingActionButton(
                        onClick = {
                            if (medicalID.isNotBlank()) {
                                healthViewModel.retrieveHealthData(
                                    medicalID = medicalID,
                                    context = context
                                ) { data ->
                                    name = data.name
                                    bloodGroup = data.bloodGroup
                                    age = data.age.toString()
                                    systolicBP = data.systolicBP
                                    diastolicBP = data.diastolicBP
                                    bloodSugarLevel = data.bloodSugarLevel.toString()
                                    weight = data.weight.toString()
                                    height = data.height.toString()
                                    gender = data.gender
                                    healthCondition = data.healthCondition
                                    allergies = data.allergies
                                    medications = data.medications
                                }
                                queryAdd =
                                    "I am $name, have $bloodGroup, $age years old," + "Blood Pressure: $systolicBP/$diastolicBP" +
                                            " weigh $weight Kg, height $height, gender $gender," +
                                            " health condition $healthCondition," +
                                            " allergies $allergies, medications $medications. "

                                loading = true
                                agentViewModel.sendQueryToAgent(queryAdd + query)
                            }
                        },
                        modifier = Modifier
                            .background(gradient, shape = RoundedCornerShape(12.dp))
                            .heightIn(min = 56.dp),
                        containerColor = Color.Transparent,
                        contentColor = Color.White,
                        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
                    ) {
                        Text(text = "Get Plan")
                    }
                }

                // Display the agent's response or show a loading indicator
                if (loading) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(60.dp),
                            strokeWidth = 4.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    agentResponse?.let { result ->
                        when {
                            result.isSuccess -> {
                                Text(
                                    text = formatText(result.getOrNull() ?: "No content"),
                                    modifier = Modifier.padding(vertical = 16.dp),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontStyle = FontStyle.Italic,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }

                            result.isFailure -> {
                                Text(
                                    text = "Error: ${result.exceptionOrNull()?.message}",
                                    modifier = Modifier.padding(vertical = 16.dp),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
                // Bottom Input Area
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(8.dp)
                        .animateContentSize(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    var isExpanded by remember { mutableStateOf(false) }

                    IconButton(
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier
                            .background(brush = gradient, shape = RoundedCornerShape(100))
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Filled.Close else Icons.Default.Add,
                            contentDescription = "Toggle add info",
                            tint = Color.White
                        )
                    }

                    AnimatedVisibility(visible = isExpanded) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = query,
                                onValueChange = { query = it },
                                label = { Text("Additional info") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = textFieldColors(),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.size(8.dp))

                            ExtendedFloatingActionButton(
                                onClick = {
                                    loading = true
                                    agentViewModel.sendQueryToAgent(queryAdd + query)
                                },
                                containerColor = Color.Transparent,
                                contentColor = Color.White,
                                modifier = Modifier.background(
                                    brush = gradient,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
                            ) {
                                Text("Send")
                            }
                        }
                    }
                }

        }

        LaunchedEffect(agentResponse) {
            agentResponse?.let { loading = false }
        }
    }
}

@Composable
fun textFieldColors() = TextFieldDefaults.colors(
    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    cursorColor = MaterialTheme.colorScheme.primary,
    focusedLabelColor = MaterialTheme.colorScheme.primary,
    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
)

//@SuppressLint("ViewModelConstructorInComposable")
//@Preview(showBackground = true)
//@Composable
//fun AgentScreenPreview() {
//    AgentScreen(navController = rememberNavController(), agentViewModel = AgentViewModel(), healthViewModel = HealthViewModel(
//        HealthRepositoryImpl(Firebase.firestore, Firebase.auth)))
//}
