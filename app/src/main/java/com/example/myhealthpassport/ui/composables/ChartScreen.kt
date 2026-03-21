package com.example.myhealthpassport.ui.composables

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.*
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.myhealthpassport.viewmodels.AiViewModel
import com.example.myhealthpassport.viewmodels.HealthViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

@Composable
fun ChartScreen(navController: NavController) {

    val adaptiveInfo = currentWindowAdaptiveInfo(
        supportLargeAndXLargeWidth = true
    )

    val windowSizeClass = adaptiveInfo.windowSizeClass

    val isExpanded =
        windowSizeClass.isWidthAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
        )

    val horizontalPadding = when {
        windowSizeClass.isWidthAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
        ) -> 96.dp

        windowSizeClass.isWidthAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
        ) -> 48.dp

        else -> 16.dp
    }

//    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
//
//        val screenWidth = maxWidth
//        val isTablet = screenWidth > 600.dp
//
//        val horizontalPadding = when {
//            screenWidth > 900.dp -> 96.dp
//            screenWidth > 600.dp -> 48.dp
//            else -> 16.dp
//        }

        ChartContent(
            isExpanded = isExpanded,
            horizontalPadding = horizontalPadding
        )

//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartContent(
    isExpanded: Boolean,
    horizontalPadding: Dp
) {
    val context = LocalContext.current
    val medicalIDs = remember { mutableStateListOf<String>() }
    val healthViewModel: HealthViewModel = viewModel()
    val aiViewModel: AiViewModel = viewModel()

    val bloodPressureList = remember { mutableStateListOf<Triple<Timestamp,Int, Int>>() }
    val bloodSugarLevelList = remember { mutableStateListOf<Pair<Timestamp, Int>>() }
    val medications = remember { mutableStateMapOf<String, Int>() }

    var bloodPressureAnalysis by remember { mutableStateOf("") }
    var bloodSugarAnalysis by remember { mutableStateOf("") }
    var medicationAnalysis by remember { mutableStateOf("") }

    var isInitialLoading by remember { mutableStateOf(true) }

    fun Timestamp.toFloatDate(): Float {
        return this.seconds.toFloat() / (24 * 60 * 60)
    }

    var prompt = """Analyze the following health data and provide insights. Identify any trends, potential health risks, and suggest recommendations for improvement. If the data indicates an abnormal pattern, highlight it with possible causes and solutions. Keep the explanation simple and actionable.
Ensure the analysis is easy to understand. Summarize key takeaways in a concise format and include any necessary health precautions."""

    LaunchedEffect(Unit) {
        healthViewModel.fetchMedicalIDs(context) { ids ->
            if (ids.isEmpty()) {
                isInitialLoading = false
            } else {
                var loadedCount = 0
                ids.forEach { id ->
                    healthViewModel.retrieveHealthData(id, context) { data ->
                        bloodPressureList.add(Triple(data.timestamp, data.systolicBP, data.diastolicBP))
                        bloodPressureList.sortBy { it.first.seconds }
                        bloodSugarLevelList.add(Pair(data.timestamp, data.bloodSugarLevel))
                        bloodSugarLevelList.sortBy { it.first.seconds }
                        data.medications.split(",").forEach { med ->
                            val trimmedMed = med.trim()
                            medications[trimmedMed] = medications.getOrDefault(trimmedMed, 0) + 1
                        }
                        loadedCount++
                        if (loadedCount == ids.size) {
                            isInitialLoading = false
                        }
                    }
                }
            }
        }
    }

    Log.d("med", medications.toString())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF80D0C7), Color(0xFF0093E9))
                )
            )
    ) {
        if (isInitialLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        } else if (bloodPressureList.isEmpty() && bloodSugarLevelList.isEmpty() && medications.isEmpty()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontalPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No health data found",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Add your medical records to see your progress charts and health insights.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            if (!isExpanded) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontalPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    item {
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
                                    aiViewModel.analyzeData(
                                        "$prompt \n" +
                                                "Blood Pressure Readings (Systolic/Diastolic in mmHg): $bloodPressureList"
                                    ) { response ->
                                        bloodPressureAnalysis = response
                                    }
                                },
                                aiResponse = bloodPressureAnalysis
                            )
                        }

                        if (bloodSugarLevelList.isNotEmpty()) {
                            ChartCard(
                                title = "Blood Sugar Levels",
                                content = { BloodSugarChart(bloodSugarLevelList) },
                                analyzeData = {
                                    aiViewModel.analyzeData(
                                        "$prompt \n" +
                                                "Blood Sugar Levels (mg/dL): $bloodSugarLevelList"
                                    ) { response ->
                                        bloodSugarAnalysis = response
                                    }
                                },
                                aiResponse = bloodSugarAnalysis
                            )
                        }

                        if (medications.isNotEmpty()) {
                            ChartCard(
                                title = "Medication Distribution",
                                content = { MedicationPieChart(medications) },
                                analyzeData = {
                                    aiViewModel.analyzeData(
                                        "$prompt \n" +
                                                "Medication List: $medications"
                                    ) { response ->
                                        medicationAnalysis = response
                                    }
                                },
                                aiResponse = medicationAnalysis
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontalPadding),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                            ) {


                                Text(
                                    text = "📊 Health Charts",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )


                                if (bloodPressureList.isNotEmpty()) {
                                    ChartCard(
                                        title = "Blood Pressure Trends",
                                        content = { BloodPressureChart(bloodPressureList) },
                                        analyzeData = {
                                            aiViewModel.analyzeData(
                                                "$prompt \nBlood Pressure Readings: $bloodPressureList"
                                            ) { response ->
                                                bloodPressureAnalysis = response
                                            }
                                        },
                                        aiResponse = bloodPressureAnalysis
                                    )
                                }


                                if (bloodSugarLevelList.isNotEmpty()) {
                                    ChartCard(
                                        title = "Blood Sugar Levels",
                                        content = { BloodSugarChart(bloodSugarLevelList) },
                                        analyzeData = {
                                            aiViewModel.analyzeData(
                                                "$prompt \nBlood Sugar Levels: $bloodSugarLevelList"
                                            ) { response ->
                                                bloodSugarAnalysis = response
                                            }
                                        },
                                        aiResponse = bloodSugarAnalysis
                                    )
                                }

                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                            ) {

                                if (medications.isNotEmpty()) {
                                    ChartCard(
                                        title = "Medication Distribution",
                                        content = { MedicationPieChart(medications) },
                                        analyzeData = {
                                            aiViewModel.analyzeData(
                                                "$prompt \nMedication List: $medications"
                                            ) { response ->
                                                medicationAnalysis = response
                                            }
                                        },
                                        aiResponse = medicationAnalysis
                                    )
                                }
                            }
                        }
                    }
                }
            }
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
    }
    }

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
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
//                    .aspectRatio(1f)
            ) {
                content()

                // Right-edge overlay to visually align the chart inside the rounded card
//                Box(
//                    modifier = Modifier
////                        .fillMaxSize()
////                        .fillMaxHeight()
//                        .height(336.dp)
//                        .width(20.dp)
//                        .background(Color.White)
//                        .align(Alignment.CenterEnd)
////                        .offset(x = (-20).dp)
//                        .zIndex(1f)
//                )
            }
//            content()

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
fun BloodPressureChart(bloodPressureList: List<Triple<Timestamp, Int, Int>>) {
    val processedList = bloodPressureList.map { Pair(it.second, it.third) }

    Log.d("Process", "Blood pressure chart data: $processedList")
    val pointsSystolic = processedList.mapIndexed { index, pair -> Point(index.toFloat(), pair.first.toFloat()) }
    val pointsDiastolic = processedList.mapIndexed { index, pair -> Point(index.toFloat(), pair.second.toFloat()) }

    val isVisible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible.value = true }

    val minBP = processedList.minOfOrNull { min(it.first, it.second) } ?: 50
    val maxBP = processedList.maxOfOrNull { max(it.first, it.second) } ?: 180
    val stepSize = (maxBP - minBP) / 4
    val dateFormatter = SimpleDateFormat("MMM dd", Locale.getDefault())
    val xLabels = bloodPressureList.map { dateFormatter.format(it.first.toDate()) }

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
//            .axisStepSize(60.dp)
            .axisStepSize(
                if (xLabels.size < 5) 80.dp else 60.dp
            )
            .axisLabelColor(Color.Black)
            .axisLineColor(Color.Gray)
            .steps(xLabels.size - 1)
            .labelData { i -> xLabels[i] }
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
//                .fillMaxSize()
//                .height(320.dp)
                .fillMaxWidth()
                .aspectRatio(1.6f)
                .background(Color.White)
                .graphicsLayer(alpha = if (isVisible.value) 1f else 0f),
            lineChartData = lineChartData
        )
    }
}

/** 📈 Blood Sugar Chart **/
@Composable
fun BloodSugarChart(bloodSugarLevelList: List<Pair<Timestamp, Int>>) {
    val processedList = bloodSugarLevelList.map { it.second }

    Log.d("Process", "Blood pressure chart data: $processedList")
    val points = processedList.mapIndexed { index, level -> Point(index.toFloat(), level.toFloat()) }

    val uniqueBloodSugarValues = processedList.distinct().sorted()

    val isVisible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible.value = true }

    val dateFormatter = SimpleDateFormat("MMM dd", Locale.getDefault())

    val xLabels = bloodSugarLevelList.map { dateFormatter.format(it.first.toDate()) }

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(lines = listOf(Line(points, LineStyle(color = Color(0xFF388E3C))))),
        xAxisData = AxisData.Builder()
            .steps(xLabels.size - 1)
//            .axisStepSize(60.dp)
            .axisStepSize(
                if (xLabels.size < 5) 80.dp else 60.dp
            )
            .labelData { i -> xLabels[i] }
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
//                .fillMaxSize()
//                .height(320.dp)
                .fillMaxWidth()
                .aspectRatio(1.6f)
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
//                .size(320.dp)
//                .fillMaxSize()
//                .fillMaxWidth(1f)
                .fillMaxWidth()
                .aspectRatio(1f)
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
                    Text(medication + " - \t Amount: " + medications[medication], style = MaterialTheme.typography.bodyMedium, color = Color.Black)
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
@PreviewScreenSizes
@Composable
fun Testc(){
    ChartScreen(rememberNavController())
}
