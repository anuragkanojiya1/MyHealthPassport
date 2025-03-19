package com.example.myhealthpassport.Composables

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myhealthpassport.R
import com.example.myhealthpassport.ViewModels.AiViewModel
import com.example.myhealthpassport.ViewModels.HealthViewModel
import dev.romainguy.kotlin.math.all
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthAiScreen(navController: NavController, aiViewModel: AiViewModel = viewModel()){

    val healthViewModel: HealthViewModel = viewModel()

    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val placeholderResult = stringResource(R.string.results_placeholder)
    val prompt2 = stringResource(R.string.prompt2)
    val prompt1 = stringResource(R.string.prompt1)
    var result by rememberSaveable { mutableStateOf(placeholderResult) }
    val uiState by aiViewModel.uiState.collectAsState()

    var userHealthData by remember { mutableStateOf<UserHealthData?>(null) }
    var medicalID by rememberSaveable { mutableStateOf("") }

    var isExtraction by remember { mutableStateOf(false) }
    var isAnalysis by remember { mutableStateOf(false) }

    val gradient2 = Brush.horizontalGradient(
        colors = listOf(Color(0xFF00BCD4), Color(0xFF1E88E5))
    )

    val gradient = Brush.linearGradient(
        colors = listOf(Color(0xFFF2F5F7),Color(0xFF7FE2F0))
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

    val getImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            bitmap = BitmapFactory.decodeStream(inputStream)
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.READ_MEDIA_IMAGES] ?: false
                || permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false

        if (granted) {
            openGallery(context, getImageLauncher)
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES)
                != PermissionChecker.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
            }
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PermissionChecker.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Medical Report Analyser",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(28.dp)
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally),
            color = Color.Black,
            fontSize = 32.sp,
            fontStyle = FontStyle.Normal
        )

        ExtendedFloatingActionButton(
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(
                            context, Manifest.permission.READ_MEDIA_IMAGES
                        ) == PermissionChecker.PERMISSION_GRANTED
                    ) {
                        openGallery(context, getImageLauncher)
                    } else {
                        requestPermissionLauncher.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(
                            context, Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PermissionChecker.PERMISSION_GRANTED
                    ) {
                        openGallery(context, getImageLauncher)
                    } else {
                        requestPermissionLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                    }
                }
            },
            modifier = Modifier
                .padding(vertical = 16.dp)
                .background(brush = gradient2, shape = RoundedCornerShape(8.dp))
                .align(Alignment.CenterHorizontally),
            containerColor = Color(0xFFE9E9F9)
        ) {
            Text(text = "Select an Image", color = Color.Black)
        }

        selectedImageUri?.let {
            bitmap?.asImageBitmap()?.let { it1 ->
                Image(
                    bitmap = it1,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(250.dp)
                        .padding(16.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            ElevatedButton(
                onClick = {
                    bitmap?.let { bmp ->
                        aiViewModel.sendPrompt(bmp, prompt2)
                    }
                    isExtraction=true
                    isAnalysis=false
                }
                ,
                enabled = prompt2.isNotEmpty() && bitmap != null,
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterVertically),
                colors = ButtonColors(
                    disabledContainerColor = Color.Red,
                    containerColor = Color.Blue,
                    contentColor = Color.White,
                    disabledContentColor = Color.White
                ),
                border = BorderStroke(2.dp, Color.White)
            ) {
                Text(text = "Extraction")
            }

            ElevatedButton(
                onClick = {
                    bitmap?.let { bmp ->
                        aiViewModel.sendPrompt(bmp, prompt1)
                    }
                    isAnalysis=true
                    isExtraction=false
                },
                enabled = prompt1.isNotEmpty() && bitmap != null,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(4.dp),
                colors = ButtonColors(
                    disabledContainerColor = Color.Red,
                    containerColor = Color.Blue,
                    contentColor = Color.White,
                    disabledContentColor = Color.White
                ),
                border = BorderStroke(2.dp, Color.White)
            ) {
                Text(text = "Analyze")
            }
        }

        if (uiState is UiState.Loading) {

            Row(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isExtraction) {
                    Text(text = "Extracting Data...", modifier = Modifier.padding(end = 16.dp))
                } else if (isAnalysis) {
                    Text(text = "Analyzing Data...", modifier = Modifier.padding(end = 16.dp))
                }
                CircularProgressIndicator(modifier = Modifier)
            }
        } else {
            var textColor = Color.Black
            if (uiState is UiState.Error) {
                textColor = MaterialTheme.colorScheme.error
                result = (uiState as UiState.Error).errorMessage
            } else if (uiState is UiState.Success) {
                textColor = MaterialTheme.colorScheme.onSurface
                result = (uiState as UiState.Success).outputText
            }
                if (isExtraction==true) {
                    val extractedJson = aiViewModel.extractJsonObject(result.trimIndent())
                    userHealthData = aiViewModel.extractMedicalReport(extractedJson)


                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Extracted Data:\n",
                            modifier = Modifier.padding(top = 16.dp, end = 16.dp),
                            color = Color.Black
                        )
                        userHealthData.let {
                            Text("Name: ${it?.name}", color = Color.Black)
                            Text("Blood Group: ${it?.bloodGroup}", color = Color.Black)
                            Text("Age: ${it?.age}", color = Color.Black)
                            Text("Blood Pressure: ${it?.systolicBP}/${it?.diastolicBP}", color = Color.Black)
                            Text("Blood Sugar Level: ${it?.bloodSugarLevel}", color = Color.Black)
                            Text("Weight: ${it?.weight}", color = Color.Black)
                            Text("Height: ${it?.height}", color = Color.Black)
                            Text("Gender: ${it?.gender}", color = Color.Black)
                            Text("Health Condition: ${it?.healthCondition}", color = Color.Black)
                            Text(
                                "Emergency Phone: ${it?.emergencyPhoneNumber}",
                                color = Color.Black
                            )
                            Text("Address: ${it?.address}", color = Color.Black)
                            Text("Allergies: ${it?.allergies}", color = Color.Black)
                            Text("Medications: ${it?.medications}", color = Color.Black)
                        }
                    }

                    if (userHealthData != null) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text("Enter Medical ID:",
                                fontWeight = FontWeight.W500,
                                color = Color.Black,
                                modifier = Modifier.align(Alignment.Start),
                                fontSize = 16.sp)

                            Row(modifier = rowModifier,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = medicalID,
                                    onValueChange = { medicalID = it },
                                    label = { Text("Medical ID", color = Color.Gray) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(12.dp))
                                        .background(Color.Transparent),
                                    colors = outlinedFieldColors,
                                    textStyle = TextStyle(fontSize = 18.sp),
                                    )
                            }

                            ExtendedFloatingActionButton(
                                onClick = {
                                    userHealthData?.let {
                                        healthViewModel.saveHealthData(
                                            it.copy(medicalID = medicalID),
                                            context
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .padding(top = 12.dp)
                                    .background(brush = gradient2, shape = RoundedCornerShape(8.dp))
                                    .align(Alignment.CenterHorizontally),
                                containerColor = Color(0xFFE9E9F9)
                            ) {
                                Text("Save Data")
                            }
                        }
                    }
                }

            if (isAnalysis==true){
                Text(
                    text = formatText(result).text,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 4.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxSize(),
                    color = Color.Black
                )
            }
        }
    }
    }

private fun openGallery(context: Context, getImageLauncher: ActivityResultLauncher<String>) {
    getImageLauncher.launch("image/*")
}

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
fun HealthAiScreenPreview(){
    HealthAiScreen(navController = rememberNavController())
}

