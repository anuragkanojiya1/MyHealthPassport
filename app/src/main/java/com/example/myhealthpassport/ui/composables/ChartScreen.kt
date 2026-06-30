package com.example.myhealthpassport.ui.composables

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.*
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.myhealthpassport.R
import com.example.myhealthpassport.ui.components.formatText
import com.example.myhealthpassport.viewmodels.AiViewModel
import com.example.myhealthpassport.viewmodels.HealthViewModel
import com.example.myhealthpassport.ui.theme.HealthBlue
import com.example.myhealthpassport.ui.theme.HealthBlueDark
import com.example.myhealthpassport.ui.theme.MyHealthPassportTheme
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

@Composable
fun ChartScreen(navController: NavController) {
    val context = LocalContext.current
    val healthViewModel: HealthViewModel = hiltViewModel()
    val aiViewModel: AiViewModel = hiltViewModel()

    val adaptiveInfo = currentWindowAdaptiveInfo()
    val windowSizeClass = adaptiveInfo.windowSizeClass
    val isExpanded = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED
    val horizontalPadding = when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.EXPANDED -> 96.dp
        WindowWidthSizeClass.MEDIUM -> 48.dp
        else -> 16.dp
    }

    val bloodPressureList = remember { mutableStateListOf<Triple<Timestamp, Int, Int>>() }
    val bloodSugarLevelList = remember { mutableStateListOf<Pair<Timestamp, Int>>() }
    val medications = remember { mutableStateMapOf<String, Int>() }

    var bloodPressureAnalysis by remember { mutableStateOf("") }
    var bloodSugarAnalysis by remember { mutableStateOf("") }
    var medicationAnalysis by remember { mutableStateOf("") }

    var isInitialLoading by remember { mutableStateOf(true) }

    val tts = remember { TextToSpeech(context) { } }
    DisposableEffect(tts) {
        onDispose { tts.shutdown() }
    }

    val prompt = """Analyze the following health data and provide insights. Identify any trends, potential health risks, and suggest recommendations for improvement. Keep it simple and actionable."""

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
                            if (trimmedMed.isNotEmpty() && trimmedMed != "null") {
                                medications[trimmedMed] = medications.getOrDefault(trimmedMed, 0) + 1
                            }
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

        ChartContent(
            isExpanded = isExpanded,
            horizontalPadding = horizontalPadding,
            bloodPressureList = bloodPressureList,
            bloodSugarLevelList = bloodSugarLevelList,
            medications = medications,
            bloodPressureAnalysis = bloodPressureAnalysis,
            bloodSugarAnalysis = bloodSugarAnalysis,
            medicationAnalysis = medicationAnalysis,
            isInitialLoading = isInitialLoading,
            onAnalyzeBloodPressure = {
                aiViewModel.analyzeData("$prompt \nBlood Pressure: $bloodPressureList") { response ->
                    bloodPressureAnalysis = response
                }
            },
            onAnalyzeBloodSugar = {
                aiViewModel.analyzeData("$prompt \nBlood Sugar Levels: $bloodSugarLevelList") { response ->
                    bloodSugarAnalysis = response
                }
            },
            onAnalyzeMedications = {
                aiViewModel.analyzeData("$prompt \nMedications: $medications") { response ->
                    medicationAnalysis = response
                }
            },
            onListen = { text ->
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartContent(
    isExpanded: Boolean,
    horizontalPadding: Dp,
    bloodPressureList: List<Triple<Timestamp, Int, Int>>,
    bloodSugarLevelList: List<Pair<Timestamp, Int>>,
    medications: Map<String, Int>,
    bloodPressureAnalysis: String,
    bloodSugarAnalysis: String,
    medicationAnalysis: String,
    isInitialLoading: Boolean,
    onAnalyzeBloodPressure: () -> Unit,
    onAnalyzeBloodSugar: () -> Unit,
    onAnalyzeMedications: () -> Unit,
    onListen: (String) -> Unit
) {

//    val backgroundPainter: Painter = painterResource(id = R.drawable.healthcare)

    Log.d("ChartScreen", "Blood Pressure Data: $bloodPressureList")
    Log.d("ChartScreen", "Blood Sugar Data: $bloodSugarLevelList")
    Log.d("ChartScreen", "Medications: $medications")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
//        Image(
//            painter = backgroundPainter,
//            contentDescription = null,
//            contentScale = ContentScale.Fit,
//            modifier = Modifier.matchParentSize().alpha(0.35f),
//        )
        if (isInitialLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
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
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Add your medical records to see your progress charts and health insights.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = horizontalPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(vertical = 24.dp)
            ) {
                item {
                    Text(
                        text = "📊 Health Analytics",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (bloodPressureList.isNotEmpty()) {
                    item {
                        ChartCard(
                            title = "Blood Pressure Trends",
                            content = { BloodPressureChart(bloodPressureList) },
                            analyzeData = onAnalyzeBloodPressure,
                            aiResponse = bloodPressureAnalysis,
                            onListen = onListen
                        )
                    }
                }

                if (bloodSugarLevelList.isNotEmpty()) {
                    item {
                        ChartCard(
                            title = "Blood Sugar Levels",
                            content = { BloodSugarChart(bloodSugarLevelList) },
                            analyzeData = onAnalyzeBloodSugar,
                            aiResponse = bloodSugarAnalysis,
                            onListen = onListen
                        )
                    }
                }

                if (medications.isNotEmpty()) {
                    item {
                        ChartCard(
                            title = "Medication Distribution",
                            content = { MedicationPieChart(medications) },
                            analyzeData = onAnalyzeMedications,
                            aiResponse = medicationAnalysis,
                            chartName = "MedicationPieChart",
                            medications = medications,
                            onListen = onListen
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChartCard(
    title: String,
    content: @Composable () -> Unit,
    analyzeData: () -> Unit,
    aiResponse: String,
    onListen: (String) -> Unit,
    chartName: String = "",
    medications: Map<String, Int> = emptyMap()
) {
    var isLoading by remember { mutableStateOf(false) }

    val gradient = Brush.horizontalGradient(listOf(HealthBlue, HealthBlueDark))

    LaunchedEffect(aiResponse) { if (aiResponse.isNotEmpty()) isLoading = false }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(
                    if (chartName == "MedicationPieChart") {
                        (298.dp + (medications.size * 40).dp)
                    } else {
                        252.dp
                    }
                )) {
                content()
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { isLoading = true; analyzeData() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(gradient),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text("Get AI Insights \uD83D\uDCA1", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (aiResponse.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = formatText(aiResponse),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                OutlinedButton(
                    onClick = { onListen(aiResponse) },
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("🔊 Listen to Analysis")
                }
            }
        }
    }
}

@Composable
fun BloodPressureChart(bloodPressureList: List<Triple<Timestamp, Int, Int>>) {
    val processedList = bloodPressureList.map { Pair(it.second, it.third) }
    
    // Adjusted range to ensure standard thresholds (130/80) are visible
    val minBP = min(60f, (processedList.minOfOrNull { min(it.first, it.second) } ?: 60).toFloat())
    val maxBP = max(150f, (processedList.maxOfOrNull { max(it.first, it.second) } ?: 150).toFloat())
    val range = max(1f, maxBP - minBP)
    val stepSize = range / 4f

    val pointsSystolic = processedList.mapIndexed { index, pair -> 
        Point(index.toFloat(), (pair.first.toFloat() - minBP) / stepSize) 
    }
    val pointsDiastolic = processedList.mapIndexed { index, pair -> 
        Point(index.toFloat(), (pair.second.toFloat() - minBP) / stepSize) 
    }

    val dateFormatter = SimpleDateFormat("MMM dd", Locale.getDefault())
    val xLabels = bloodPressureList.map { dateFormatter.format(it.first.toDate()) }

    val chartColor = MaterialTheme.colorScheme.onSurface
    val gridColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                // Scale line to ensure 0..4 units (covering the 4 Y-axis steps)
                Line(
                    listOf(Point(0f, 0f), Point(0f, 4f)),
                    LineStyle(color = Color.Transparent),
                    null, null, null
                ),
                Line(pointsSystolic, LineStyle(color = HealthBlue),
                    IntersectionPoint(HealthBlue),
                    SelectionHighlightPoint(HealthBlue),
                    shadowUnderLine = ShadowUnderLine(HealthBlue.copy(alpha = 0.1f))
                ),
                Line(pointsDiastolic, LineStyle(color = HealthBlueDark),
                    IntersectionPoint(HealthBlueDark),
                    SelectionHighlightPoint(HealthBlueDark),
                    shadowUnderLine = ShadowUnderLine(HealthBlueDark.copy(alpha = 0.1f))
                ),
                // Threshold line for Systolic (130 mmHg)
                Line(
                    List(pointsSystolic.size) { i -> Point(i.toFloat(), (130f - minBP) / stepSize) },
                    LineStyle(color = Color.Red.copy(alpha = 0.4f), lineType = LineType.Straight()),
                    null, null, null
                ),
                // Threshold line for Diastolic (80 mmHg)
                Line(
                    List(pointsDiastolic.size) { i -> Point(i.toFloat(), (80f - minBP) / stepSize) },
                    LineStyle(color = Color.Red.copy(alpha = 0.4f), lineType = LineType.Straight()),
                    null, null, null
                )
            )
        ),
        xAxisData = AxisData.Builder()
            .axisStepSize(if (xLabels.size < 5) 80.dp else 60.dp)
            .axisLabelColor(chartColor)
            .axisLineColor(gridColor)
            .backgroundColor(Color.Transparent)
            .startDrawPadding(20.dp)
            .steps(max(1, xLabels.size - 1))
            .labelData { i -> if (i < xLabels.size) xLabels[i] else "" }
            .build(),
        yAxisData = AxisData.Builder()
            .steps(4)
            .axisLabelColor(chartColor)
            .axisLineColor(gridColor)
            .labelData { i -> "${(minBP + (i * stepSize)).toInt()}" }
            .backgroundColor(Color.Transparent)
            .build(),
        gridLines = GridLines(color = gridColor),
        backgroundColor = Color.Transparent
    )

    LineChart(
        modifier = Modifier.fillMaxSize(),
        lineChartData = lineChartData
    )
}

@Composable
fun BloodSugarChart(bloodSugarLevelList: List<Pair<Timestamp, Int>>) {
    if (bloodSugarLevelList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No data", style = MaterialTheme.typography.bodySmall)
        }
        return
    }

    val processedList = bloodSugarLevelList.map { it.second }
    
    // Ensure the chart shows standard thresholds (100 fasting, 140 post-meal)
    val minVal = 0f
    val maxVal = max(160f, (processedList.maxOrNull()?.toFloat() ?: 100f))
    val range = max(1f, maxVal - minVal)
    val stepSize = range / 4f

    val points = bloodSugarLevelList.mapIndexed { index, pair -> 
        Point(index.toFloat(), (pair.second.toFloat() - minVal) / stepSize) 
    }

    val dateFormatter = SimpleDateFormat("MMM dd", Locale.getDefault())
    val xLabels = bloodSugarLevelList.map { dateFormatter.format(it.first.toDate()) }

    val chartColor = MaterialTheme.colorScheme.onSurface
    val gridColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)

    val xSteps = max(1, xLabels.size - 1)

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                // Scale line to ensure 0..4 units (covering the 4 Y-axis steps)
                Line(
                    listOf(Point(0f, 0f), Point(0f, 4f)),
                    LineStyle(color = Color.Transparent),
                    null, null, null
                ),
                Line(
                    points,
                    LineStyle(color = HealthBlueDark, lineType = LineType.SmoothCurve()),
                    IntersectionPoint(HealthBlueDark),
                    SelectionHighlightPoint(HealthBlueDark),
                    shadowUnderLine = ShadowUnderLine(HealthBlueDark.copy(alpha = 0.1f))
                ),
                // Threshold line at 140 mg/dL (Normal post-meal limit)
                Line(
                    List(points.size) { i -> Point(i.toFloat(), (140f - minVal) / stepSize) },
                    LineStyle(color = Color.Red.copy(alpha = 0.4f), lineType = LineType.Straight()),
                    null,
                    null,
                    null
                ),
                // Threshold line at 100 mg/dL (Normal fasting limit)
                Line(
                    List(points.size) { i -> Point(i.toFloat(), (100f - minVal) / stepSize) },
                    LineStyle(color = Color.Red.copy(alpha = 0.4f), lineType = LineType.Straight()),
                    null,
                    null,
                    null
                )
            )
        ),
        xAxisData = AxisData.Builder()
            .steps(xSteps)
            .axisStepSize(if (xLabels.size < 5) 80.dp else 60.dp)
            .labelData { i -> if (i < xLabels.size) xLabels[i] else "" }
            .axisLabelColor(chartColor)
            .axisLineColor(gridColor)
            .backgroundColor(Color.Transparent)
            .startDrawPadding(20.dp)
            .build(),
        yAxisData = AxisData.Builder()
            .steps(4)
            .axisLabelColor(chartColor)
            .axisLineColor(gridColor)
            .backgroundColor(Color.Transparent)
            .labelData { i -> "${(minVal + (i * stepSize)).toInt()}" }
            .build(),
        gridLines = GridLines(color = gridColor),
        backgroundColor = Color.Transparent
    )

    LineChart(
        modifier = Modifier.fillMaxSize(),
        lineChartData = lineChartData
    )
}

@Composable
fun MedicationPieChart(medications: Map<String, Int>) {
    // Keep Animatable per medication and adapt when medications map changes
    // Keep a remembered map of colors per medication (no Animatable) so colors persist across recompositions
    val colorMap = remember { mutableMapOf<String, Color>() }

    LaunchedEffect(medications.keys) {
        // assign a color for any new medication
        medications.keys.forEach { key ->
            if (!colorMap.containsKey(key)) {
                colorMap[key] = getRandomColor()
            }
        }
        // remove colors for meds that no longer exist
        val toRemove = colorMap.keys.filter { it !in medications.keys }
        toRemove.forEach { colorMap.remove(it) }
    }

    val pieSlices = medications.map {
        PieChartData.Slice(it.key, it.value.toFloat(), colorMap[it.key] ?: Color.Gray)
    }

    val pieChartConfig = PieChartConfig(
        isAnimationEnable = true,
        showSliceLabels = true,
        animationDuration = 1500,
        backgroundColor = Color.Transparent
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        PieChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(298.dp),
            pieChartData = PieChartData(slices = pieSlices, plotType = PlotType.Pie),
            pieChartConfig = pieChartConfig
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            medications.keys.forEach { medication ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(4.dp)) {
                    Box(modifier = Modifier
                        .size(12.dp)
                        .background(colorMap[medication] ?: Color.Gray, CircleShape))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$medication: ${medications[medication]}", 
                        style = MaterialTheme.typography.bodySmall, 
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

fun getRandomColor(): Color = Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f)

@Preview(showBackground = true)
@Composable
fun ChartPreview(){
    MyHealthPassportTheme {
        ChartContent(
            isExpanded = false,
            horizontalPadding = 16.dp,
            bloodPressureList = listOf(
                Triple(Timestamp.now(), 120, 80),
                Triple(Timestamp.now(), 135, 85),
                Triple(Timestamp.now(), 118, 78)
            ),
            bloodSugarLevelList = listOf(
                Pair(Timestamp.now(), 90),
                Pair(Timestamp.now(), 145),
                Pair(Timestamp.now(), 110)
            ),
            medications = mapOf("Aspirin" to 5, "Metformin" to 10),
            bloodPressureAnalysis = "BP looks mostly stable.",
            bloodSugarAnalysis = "One spike detected.",
            medicationAnalysis = "Consistent usage.",
            isInitialLoading = false,
            onAnalyzeBloodPressure = {},
            onAnalyzeBloodSugar = {},
            onAnalyzeMedications = {},
            onListen = {}
        )
    }
}
