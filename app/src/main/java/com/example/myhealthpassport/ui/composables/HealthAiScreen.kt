package com.example.myhealthpassport.ui.composables

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myhealthpassport.R
import com.example.myhealthpassport.ui.navigation.Screen
import com.example.myhealthpassport.ui.theme.HealthBlue
import com.example.myhealthpassport.ui.theme.HealthBlueDark
import com.example.myhealthpassport.ui.theme.MyHealthPassportTheme
import com.example.myhealthpassport.viewmodels.AiViewModel
import com.example.myhealthpassport.viewmodels.ApiKeyViewModel
import com.example.myhealthpassport.domain.model.UserHealthData
import java.io.InputStream

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthAiScreen(
    navController: NavController,
    aiViewModel: AiViewModel = hiltViewModel(),
    apiKeyViewModel: ApiKeyViewModel = hiltViewModel()
) {
    val brandGradient = Brush.horizontalGradient(
        colors = listOf(HealthBlue, HealthBlueDark)
    )

    val isApiKeyMissing by apiKeyViewModel.isApiKeyMissing.collectAsState()
    val uiState by aiViewModel.uiState.collectAsState()
    val medicalIds by aiViewModel.medicalIds.collectAsState()

    val prompt2 = stringResource(R.string.prompt2)
    val prompt1 = stringResource(R.string.prompt1)

    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current
    val backgroundPainter: Painter = painterResource(id = R.drawable.healthcare)

    // NAVIGATION GUARD: Redirect if key is missing
    LaunchedEffect(isApiKeyMissing) {
        if (isApiKeyMissing == true) {
            navController.navigate(Screen.ApiKeySettings.route) {
                popUpTo(Screen.HealthAiScreen.route) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    if (isApiKeyMissing == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val getImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.READ_MEDIA_IMAGES] ?: false
                || permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false

        if (granted) {
            openGallery(getImageLauncher)
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (uiState is UiState.Loading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = HealthBlue,
                trackColor = HealthBlue.copy(alpha = 0.2f)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = backgroundPainter,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.matchParentSize().alpha(0.35f),
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Text(
                    text = "Medical Report Analysis",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Choose your analysis option",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(24.dp))

                // Upload Box with responsive aspect ratio
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .aspectRatio(1.8f), // Dynamic height based on width
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(onClick = {
                                checkAndRequestPermission(
                                    context,
                                    requestPermissionLauncher,
                                    getImageLauncher
                                )
                            }),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(
                                imageVector = if (bitmap != null) Icons.Default.CheckCircle else Icons.Default.CloudUpload,
                                contentDescription = null,
                                tint = if (bitmap != null) Color(0xFF4CAF50) else HealthBlue,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = if (bitmap != null) "Report Selected" else "Tap to upload medical report",
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Supports JPG, PNG",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FeatureCard(
                        title = "Quick Save",
                        description = "AI extracts and saves data automatically",
                        icon = Icons.Default.Save,
                        gradient = brandGradient,
                        enabled = bitmap != null,
                        onClick = { aiViewModel.sendPrompt(bitmap!!, prompt2, "Save") },
                        modifier = Modifier.weight(1f)
                    )

                    FeatureCard(
                        title = "Detailed Analysis",
                        description = "Comprehensive AI analysis of your report",
                        icon = Icons.Default.Analytics,
                        gradient = brandGradient,
                        enabled = bitmap != null,
                        onClick = { aiViewModel.sendPrompt(bitmap!!, prompt1, "Analyse") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(32.dp))

                Text(
                    text = "Analysis benefits",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = HealthBlueDark,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(Modifier.height(16.dp))
                InfoItem("Detailed insights from your reports")
                InfoItem("Personalized recommendations")
                InfoItem("Trend tracking over time")
                InfoItem("Secure clinical data extraction")

                Spacer(Modifier.height(24.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "🔒 Your data is processed securely",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }

        // --- Result Overlays ---
        when (uiState) {
            is UiState.Success -> {
                ResultBottomSheet((uiState as UiState.Success).outputText) {
                    aiViewModel.resetState()
                }
            }
            is UiState.Error -> {
                ResultBottomSheet((uiState as UiState.Error).errorMessage) {
                    aiViewModel.resetState()
                }
            }
            is UiState.ExtractedData -> {
                ReviewAndSaveBottomSheet(
                    data = (uiState as UiState.ExtractedData).data,
                    medicalIds = medicalIds,
                    onSave = { updatedData ->
                        aiViewModel.saveExtractedData(updatedData)
                    },
                    onDismiss = {
                        aiViewModel.resetState()
                    }
                )
            }
            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewAndSaveBottomSheet(
    data: UserHealthData,
    medicalIds: List<String>,
    onSave: (UserHealthData) -> Unit,
    onDismiss: () -> Unit
) {
    var editedData by remember { mutableStateOf(data) }
    var showEditDialog by remember { mutableStateOf<Pair<String, (String) -> Unit>?>(null) }
    var tempValue by remember { mutableStateOf("") }
    
    var medicalIdExpanded by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showEditDialog != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = null },
            title = { Text("Edit ${showEditDialog!!.first}") },
            text = {
                OutlinedTextField(
                    value = tempValue,
                    onValueChange = { tempValue = it },
                    label = { Text(showEditDialog!!.first) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            },
            confirmButton = {
                Button(onClick = {
                    showEditDialog!!.second(tempValue)
                    showEditDialog = null
                }) {
                    Text("Update")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 40.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Review & Edit Data",
                style = MaterialTheme.typography.headlineSmall,
                color = HealthBlueDark,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Tap on any field to edit its value. Choose an existing Medical ID or enter a new one.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Medical ID Selection
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = editedData.medicalID,
                    onValueChange = { editedData = editedData.copy(medicalID = it) },
                    label = { Text("Medical ID (Required)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        IconButton(onClick = { medicalIdExpanded = !medicalIdExpanded }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    isError = editedData.medicalID.isBlank()
                )
                DropdownMenu(
                    expanded = medicalIdExpanded,
                    onDismissRequest = { medicalIdExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    medicalIds.forEach { id ->
                        DropdownMenuItem(
                            text = { Text(id) },
                            onClick = {
                                editedData = editedData.copy(medicalID = id)
                                medicalIdExpanded = false
                            }
                        )
                    }
                }
            }

            // Editable Fields List
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column {
                    EditableDetailRow("Patient Name", editedData.name) {
                        tempValue = editedData.name
                        showEditDialog = "Patient Name" to { editedData = editedData.copy(name = it) }
                    }
                    HorizontalDivider()
                    EditableDetailRow("Age", editedData.age.toString()) {
                        tempValue = editedData.age.toString()
                        showEditDialog = "Age" to { editedData = editedData.copy(age = it.toIntOrNull() ?: 0) }
                    }
                    HorizontalDivider()
                    EditableDetailRow("Blood Group", editedData.bloodGroup) {
                        tempValue = editedData.bloodGroup
                        showEditDialog = "Blood Group" to { editedData = editedData.copy(bloodGroup = it) }
                    }
                    HorizontalDivider()
                    EditableDetailRow("BP (Systolic)", editedData.systolicBP.toString()) {
                        tempValue = editedData.systolicBP.toString()
                        showEditDialog = "BP (Systolic)" to { editedData = editedData.copy(systolicBP = it.toIntOrNull() ?: 0) }
                    }
                    HorizontalDivider()
                    EditableDetailRow("BP (Diastolic)", editedData.diastolicBP.toString()) {
                        tempValue = editedData.diastolicBP.toString()
                        showEditDialog = "BP (Diastolic)" to { editedData = editedData.copy(diastolicBP = it.toIntOrNull() ?: 0) }
                    }
                    HorizontalDivider()
                    EditableDetailRow("Blood Sugar", editedData.bloodSugarLevel.toString()) {
                        tempValue = editedData.bloodSugarLevel.toString()
                        showEditDialog = "Blood Sugar" to { editedData = editedData.copy(bloodSugarLevel = it.toIntOrNull() ?: 0) }
                    }
                    HorizontalDivider()
                    EditableDetailRow("Weight (kg)", editedData.weight.toString()) {
                        tempValue = editedData.weight.toString()
                        showEditDialog = "Weight" to { editedData = editedData.copy(weight = it.toFloatOrNull() ?: 0.0f) }
                    }
                    HorizontalDivider()
                    EditableDetailRow("Height (cm)", editedData.height.toString()) {
                        tempValue = editedData.height.toString()
                        showEditDialog = "Height" to { editedData = editedData.copy(height = it.toFloatOrNull() ?: 0.0f) }
                    }
                    HorizontalDivider()
                    EditableDetailRow("Gender", editedData.gender) {
                        tempValue = editedData.gender
                        showEditDialog = "Gender" to { editedData = editedData.copy(gender = it) }
                    }
                    HorizontalDivider()
                    EditableDetailRow("Health Condition", editedData.healthCondition) {
                        tempValue = editedData.healthCondition
                        showEditDialog = "Health Condition" to { editedData = editedData.copy(healthCondition = it) }
                    }
                    HorizontalDivider()
                    EditableDetailRow("Emergency Phone", editedData.emergencyPhoneNumber) {
                        tempValue = editedData.emergencyPhoneNumber
                        showEditDialog = "Emergency Phone" to { editedData = editedData.copy(emergencyPhoneNumber = it) }
                    }
                    HorizontalDivider()
                    EditableDetailRow("Address", editedData.address) {
                        tempValue = editedData.address
                        showEditDialog = "Address" to { editedData = editedData.copy(address = it) }
                    }
                    HorizontalDivider()
                    EditableDetailRow("Allergies", editedData.allergies) {
                        tempValue = editedData.allergies
                        showEditDialog = "Allergies" to { editedData = editedData.copy(allergies = it) }
                    }
                    HorizontalDivider()
                    EditableDetailRow("Medications", editedData.medications) {
                        tempValue = editedData.medications
                        showEditDialog = "Medications" to { editedData = editedData.copy(medications = it) }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (editedData.medicalID.isNotBlank()) {
                        onSave(editedData)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HealthBlue),
                enabled = editedData.medicalID.isNotBlank()
            ) {
                Text("CONFIRM AND SAVE", fontWeight = FontWeight.Bold)
            }
            
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun EditableDetailRow(label: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = HealthBlue)
            Text(text = value.ifBlank { "Not specified" }, style = MaterialTheme.typography.bodyLarge)
        }
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit",
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultBottomSheet(result: String, onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 40.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Result",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = formatTextHealth(result),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 24.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("DONE")
            }
        }
    }
}

@Composable
fun FeatureCard(
    title: String,
    description: String,
    icon: ImageVector,
    gradient: Brush,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (enabled) 4.dp else 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (enabled) gradient else Brush.linearGradient(listOf(Color.LightGray, Color.Gray)))
                .clickable(enabled = enabled, onClick = onClick)
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )

                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = 18.sp
                )
            }
            
            if (!enabled) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                }
            }
        }
    }
}

@Composable
fun InfoItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically, 
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Check, 
            contentDescription = null, 
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(text = text, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium)
    }
}

private fun checkAndRequestPermission(
    context: Context,
    launcher: ActivityResultLauncher<Array<String>>,
    galleryLauncher: ActivityResultLauncher<String>
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PermissionChecker.PERMISSION_GRANTED) {
            openGallery(galleryLauncher)
        } else {
            launcher.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
        }
    } else {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED) {
            openGallery(galleryLauncher)
        } else {
            launcher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
        }
    }
}

private fun openGallery(getImageLauncher: ActivityResultLauncher<String>) {
    getImageLauncher.launch("image/*")
}

@Composable
fun formatTextHealth(text: String): AnnotatedString {
    return buildAnnotatedString {
        val parts = text.split("**")
        parts.forEachIndexed { index, part ->
            if (index % 2 == 0) {
                append(part)
            } else {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)) {
                    append(part)
                }
            }
        }
    }
}
