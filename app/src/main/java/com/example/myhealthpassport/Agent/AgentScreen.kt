import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.myhealthpassport.Navigation.Screen
import com.example.myhealthpassport.ViewModels.AgentViewModel
import com.example.myhealthpassport.ViewModels.HealthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentScreen(navController: NavController, agentViewModel: AgentViewModel, healthViewModel: HealthViewModel) {

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
        colors = listOf(Color(0xFF00BCD4), Color(0xFF1E88E5))
    )

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Get your Personalized Diet" +
                " with Exercise Recommendation Plan by our Agent...",
            modifier = Modifier.padding(16.dp)
                .align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Light,
            color = Color.Black,
            fontSize = 24.sp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.fillMaxWidth(0.4f))
            OutlinedTextField(
                modifier = Modifier.weight(0.5f),
                value = medicalID,
                onValueChange = { medicalID = it },
                label = {
                    Text(text = "MedicalID", color = Color.Gray)
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.Blue,
                    focusedTextColor = Color.Black,
                    focusedPlaceholderColor = Color.Gray,
                    errorTextColor = Color.Red
                ),
            )
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .background(gradient, shape = RoundedCornerShape(8.dp)),
                containerColor = Color(0xFFE9EFF9),
                onClick = {
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
                    queryAdd = "I am $name, have $bloodGroup, $age years old," + "Blood Pressure: $systolicBP/$diastolicBP"+
                            " weigh $weight Kg, height $height, gender $gender," +
                            " health condition $healthCondition," +
                            " allergies $allergies, medications $medications. "

                    loading = true // Set loading to true when button is clicked
                    agentViewModel.sendQueryToAgent(queryAdd + query)
                }
            ) {
                Text(text = "Get Plan", textAlign = TextAlign.Center, color = Color.Black)
            }
        }

        Column(modifier = Modifier
            .padding(horizontal = 8.dp)
            .weight(0.6f)
            .verticalScroll(rememberScrollState())
        ) {
            // Display the agent's response or show a loading indicator
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(160.dp)
                        .padding(36.dp),
                    strokeWidth = 8.dp
                )
            } else {
                agentResponse?.let { result ->
                    when {
                        result.isSuccess -> {
                            Text(
                                text = formatText(result.getOrNull() ?: "No content"),
                                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W400,
                                fontStyle = FontStyle.Italic,
                                color = Color.Black
                            )
                        }
                        result.isFailure -> {
                            Text(
                                text = "Error: ${result.exceptionOrNull()?.message}",
                                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
                                color = Color.Red
                            )
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
            verticalAlignment = Alignment.CenterVertically,
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
                    OutlinedTextField(
                        value = query,
                        onValueChange = { newValue -> query = newValue },
                        label = { Text("Add additional info", color = Color.Gray) },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(0.5f),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Blue,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.Blue,
                            focusedTextColor = Color.Black,
                            focusedPlaceholderColor = Color.Gray,
                            errorTextColor = Color.Red
                        ),
                    )

                    queryAdd = "I am $name, have $bloodGroup, $age years old," +
                            " weigh $weight Kg, height $height, gender $gender," +
                            " health condition $healthCondition," +
                            " allergies $allergies, medications $medications. "

                    // Button to trigger the query
                    ExtendedFloatingActionButton(
                        onClick = {
                            loading = true // Set loading to true when button is clicked
                            agentViewModel.sendQueryToAgent(queryAdd + query)
                        },
                        containerColor = Color(0xFFE9EFF9),
                        modifier = Modifier
                            .background(brush = gradient, shape = RoundedCornerShape(8.dp))
                    ) {
                        Text("Send Query", color = Color.Black)
                    }
                }
            }
        }


//        // Display the agent's response or show a loading indicator
//        if (loading) {
//            CircularProgressIndicator(
//                modifier = Modifier
//                    .size(160.dp)
//                    .padding(36.dp),
//                strokeWidth = 8.dp
//            )
//        } else {
//            agentResponse?.let { result ->
//                when {
//                    result.isSuccess -> {
//                        Text(
//                            text = formatText(result.getOrNull() ?: "No content"),
//                            modifier = Modifier.padding(top = 20.dp),
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.W400
//                        )
//                    }
//                    result.isFailure -> {
//                        Text(
//                            text = "Error: ${result.exceptionOrNull()?.message}",
//                            modifier = Modifier.padding(top = 20.dp),
//                            color = Color.Red
//                        )
//                    }
//                }
//            }
//        }

        // Set loading to false when response is received
        LaunchedEffect(agentResponse) {
            agentResponse?.let {
                loading = false
            }
        }
    }
}


// Function to format text by bolding and enlarging words enclosed in **
@Composable
fun formatText(text: String): AnnotatedString {
    return buildAnnotatedString {
        var currentIndex = 0

        // Split the text by **
        val parts = text.split("**")

        parts.forEachIndexed { index, part ->
            if (index % 2 == 0) {
                // Regular text (outside of **)
                append(part)
            } else {
                // Bold and larger text (inside **)
                withStyle(style = SpanStyle(fontWeight = FontWeight.W400, fontSize = 28.sp)) {
                    append(part)
                }
            }
            currentIndex += part.length
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AgentScreenPreview() {
    AgentScreen(navController = rememberNavController(), agentViewModel = AgentViewModel(), healthViewModel = HealthViewModel())
}