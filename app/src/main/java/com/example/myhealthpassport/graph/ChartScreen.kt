package com.example.myhealthpassport.graph

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.*
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.myhealthpassport.ViewModels.AiViewModel
import com.example.myhealthpassport.ViewModels.HealthViewModel
import java.util.Locale
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartScreen(navController: NavController) {
    val context = LocalContext.current
    val medicalIDs = remember { mutableStateListOf<String>() }
    val healthViewModel: HealthViewModel = viewModel()
    val aiViewModel: AiViewModel = viewModel()

    val bloodPressureList = remember { mutableStateListOf<Pair<Int, Int>>() }
    val bloodSugarLevelList = remember { mutableStateListOf<Int>() }
    val medications = remember { mutableStateMapOf<String, Int>() }

    var bloodPressureAnalysis by remember { mutableStateOf("") }
    var bloodSugarAnalysis by remember { mutableStateOf("") }
    var medicationAnalysis by remember { mutableStateOf("") }

    var prompt = """Analyze the following health data and provide insights. Identify any trends, potential health risks, and suggest recommendations for improvement. If the data indicates an abnormal pattern, highlight it with possible causes and solutions. Keep the explanation simple and actionable.
Ensure the analysis is easy to understand. Summarize key takeaways in a concise format and include any necessary health precautions."""

    LaunchedEffect(Unit) {
        healthViewModel.fetchMedicalIDs(context) { ids ->
            ids.forEach { id ->
                healthViewModel.retrieveHealthData(id, context) { data ->
                    bloodPressureList.add(Pair(data.systolicBP, data.diastolicBP))
                    bloodSugarLevelList.add(data.bloodSugarLevel)
                    data.medications.split(",").forEach { med ->
                        val trimmedMed = med.trim()
                        medications[trimmedMed] = medications.getOrDefault(trimmedMed, 0) + 1
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF80D0C7), Color(0xFF0093E9))
                )
            )
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "📊 Health Charts",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (bloodPressureList.isNotEmpty()) {
            ChartCard(
                title = "Blood Pressure Trends",
                content = { BloodPressureChart(bloodPressureList) },
                analyzeData = {
                    aiViewModel.analyzeData("$prompt \n" +
                            "Blood Pressure Readings (Systolic/Diastolic in mmHg): $bloodPressureList") { response ->
                        bloodPressureAnalysis = response
                    }
                },
                aiResponse = bloodPressureAnalysis)
        }

        if (bloodSugarLevelList.isNotEmpty()) {
            ChartCard(
                title = "Blood Sugar Levels",
                content = { BloodSugarChart(bloodSugarLevelList) },
                analyzeData = {
                    aiViewModel.analyzeData("$prompt \n" +
                            "Blood Sugar Levels (mg/dL): $bloodSugarLevelList") { response ->
                        bloodSugarAnalysis = response
                    }
                },
                aiResponse = bloodSugarAnalysis)
        }

        if (medications.isNotEmpty()) {
            ChartCard(
                title = "Medication Distribution",
                content = { MedicationPieChart(medications) },
                analyzeData = {
                    aiViewModel.analyzeData("$prompt \n" +
                            "Medication List: $medications") { response ->
                        medicationAnalysis = response
                    }
                },
                aiResponse = medicationAnalysis)
        }
    }
}

/** 🎨 Stylish Card Wrapper **/
@Composable
fun ChartCard(title: String, content: @Composable () -> Unit, analyzeData: () -> Unit, aiResponse: String) {

    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val tts = remember { TextToSpeech(context) { status ->
        if (status != TextToSpeech.SUCCESS) {
            Log.e("TTS", "Initialization failed!")
        }
    } }

    LaunchedEffect(aiResponse) {
        if (aiResponse.isNotEmpty()) {
            isLoading = false
        }
    }

    val brush = Brush.verticalGradient(
        colors = listOf(Color(0xFF80D0C7), Color(0xFF0093E9))
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp).background(Color.White)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()

            Button(
                onClick = {
                    isLoading = true
                    analyzeData()
                    },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Get Insights"+"\uD83D\uDCA1")
                }
            }

            if (aiResponse.isNotEmpty()) {
                //isLoading = false
                Text(
                    text = formatText(aiResponse),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Button(
                    onClick = {
                        tts.speak(aiResponse, TextToSpeech.QUEUE_FLUSH, null, null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("🔊 Listen")
                }
            }
        }
    }
}

/** 📈 Blood Pressure Chart **/
@Composable
fun BloodPressureChart(bloodPressureList: List<Pair<Int, Int>>) {
    val pointsSystolic = bloodPressureList.mapIndexed { index, pair -> Point(index.toFloat(), pair.first.toFloat()) }
    val pointsDiastolic = bloodPressureList.mapIndexed { index, pair -> Point(index.toFloat(), pair.second.toFloat()) }

    val isVisible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible.value = true }

    val minBP = bloodPressureList.minOfOrNull { min(it.first, it.second) } ?: 50
    val maxBP = bloodPressureList.maxOfOrNull { max(it.first, it.second) } ?: 180
    val stepSize = (maxBP - minBP) / 4

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(pointsSystolic, LineStyle(color = Color(0xFF673AB7)),
                    IntersectionPoint(Color(0xFF9C27B0)),
                    SelectionHighlightPoint(Color(0xFF9C27B0)),
                    shadowUnderLine = ShadowUnderLine(Color(0xFFE1BEE7))
                    ),
                Line(pointsDiastolic, LineStyle(color = Color(0xFF311B92)),
                    IntersectionPoint(Color(0xFF9C27B0)),
                    SelectionHighlightPoint(Color(0xFF9C27B0)),
                    shadowUnderLine = ShadowUnderLine(Color(0xFFE1BEE7))
                )
            )
        ),
        xAxisData = AxisData.Builder()
            .axisStepSize(60.dp)
            .axisLabelColor(Color.Black)
            .axisLineColor(Color.Gray)
            .steps(pointsSystolic.size - 1)
            .labelData { i -> "${i + 1}" }
            .labelAndAxisLinePadding(15.dp)
            .backgroundColor(Color.White)
            .build(),
        yAxisData = AxisData.Builder()
            .steps(4)
            .axisLabelColor(Color.Black)
            .axisLineColor(Color.Gray)
            .labelData { i -> "${(minBP + (i * stepSize)).toInt()} mmHg" }
            .labelAndAxisLinePadding(16.dp)
            .backgroundColor(Color.White)
            .build(),
        gridLines = GridLines(),
        backgroundColor = Color.White
    )

    AnimatedVisibility(visible = isVisible.value) {
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .background(Color.White)
                .graphicsLayer(alpha = if (isVisible.value) 1f else 0f),
            lineChartData = lineChartData
        )
    }
}

/** 📈 Blood Sugar Chart **/
@Composable
fun BloodSugarChart(bloodSugarLevelList: List<Int>) {
    val points = bloodSugarLevelList.mapIndexed { index, level -> Point(index.toFloat(), level.toFloat()) }

    val uniqueBloodSugarValues = bloodSugarLevelList.distinct().sorted()

    val isVisible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible.value = true }

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(lines = listOf(Line(points, LineStyle(color = Color(0xFF388E3C))))),
        xAxisData = AxisData.Builder()
            .steps(points.size - 1)
            .axisStepSize(60.dp)
            .labelData { i -> "${i + 1}" }
            .labelAndAxisLinePadding(15.dp)
            .axisLabelColor(Color.Black)
            .axisLineColor(Color.Gray)
            .backgroundColor(Color.White)
            .build(),
        yAxisData = AxisData.Builder()
            .steps(uniqueBloodSugarValues.size - 1)
            .axisLabelColor(Color.Black)
            .axisLineColor(Color.Gray)
            .backgroundColor(Color.White)
            .labelAndAxisLinePadding(16.dp)
            .labelData { i -> "${uniqueBloodSugarValues[i]} mg/dL" }
            .build(),
        gridLines = GridLines(),
        backgroundColor = Color.White
    )

    AnimatedVisibility(visible = isVisible.value) {
        LineChart(
            modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(Color.White)
            .graphicsLayer(alpha = if (isVisible.value) 1f else 0f),
            lineChartData = lineChartData
        )
    }
}

/** 🥧 Medication Pie Chart **/
@Composable
fun MedicationPieChart(medications: Map<String, Int>) {

   // val medicationColors = remember { medications.keys.associateWith { getRandomColor() } }

    val animatedColors = remember {
        medications.keys.associateWith { key ->
            Animatable(getRandomColor())
        }
    }

    LaunchedEffect(Unit) {
        animatedColors.values.forEach { animatable ->
            animatable.animateTo(
                targetValue = getRandomColor(),
                animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
            )
        }
    }

    val pieSlices = medications.map {
        PieChartData.Slice(it.key, it.value.toFloat(), animatedColors[it.key]?.value ?: Color.Gray)
    }

    val pieChartConfig = PieChartConfig(
        isAnimationEnable = true,
        showSliceLabels = true,
        animationDuration = 1500,
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        PieChart(
            modifier = Modifier
                .size(320.dp)
                .align(Alignment.CenterHorizontally),
            pieChartData = PieChartData(slices = pieSlices, plotType = PlotType.Pie),
            pieChartConfig = pieChartConfig
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.padding(8.dp)) {
            medications.keys.forEach { medication ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(4.dp)) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(animatedColors[medication]?.value ?: Color.Gray, shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(medication, style = MaterialTheme.typography.bodyMedium, color = Color.Black)
                }
            }
        }
    }
}


fun getRandomColor(): Color = Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f)

@Composable
fun formatText(text: String): AnnotatedString {
    return buildAnnotatedString {
        var currentIndex = 0

        val parts = text.split("**")

        parts.forEachIndexed { index, part ->
            if (index % 2 == 0) {
                append(part)
            } else {
                withStyle(style = SpanStyle(fontWeight = FontWeight.W400, fontSize = 20.sp)) {
                    append(part)
                }
            }
            currentIndex += part.length
        }
    }
}

@Preview(showBackground = true)
@Composable
fun testc(){
    ChartScreen(rememberNavController())
}
