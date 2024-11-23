package com.example.myhealthpassport.Health

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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
import java.io.InputStream

@Composable
fun HealthAiScreen(navController: NavController, aiViewModel: AiViewModel = viewModel()){

    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val placeholderResult = stringResource(R.string.results_placeholder)
    val prompt = stringResource(R.string.prompt)
    var result by rememberSaveable { mutableStateOf(placeholderResult) }
    val uiState by aiViewModel.uiState.collectAsState()

    val gradient2 = Brush.horizontalGradient(
        colors = listOf(Color(0xFF00BCD4), Color(0xFF1E88E5))
    )

    val gradient = Brush.linearGradient(
        colors = listOf(Color(0xFFF2F5F7),Color(0xFF7FE2F0))
    )

    // Define the image picker launcher
    val getImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            bitmap = BitmapFactory.decodeStream(inputStream)
        }
    }

    // Define the permission launcher
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, proceed with accessing gallery
            openGallery(context, getImageLauncher)
        } else {
            // Permission is denied, show an error message or dialog
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Check for permissions and request if necessary
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PermissionChecker.PERMISSION_GRANTED -> {
                    // Permission is already granted
                }
                else -> {
                    // Request permission
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        } else {
            // Permissions are automatically granted on Android versions below M
            openGallery(context, getImageLauncher)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Medical Certificate Analyser",
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
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PermissionChecker.PERMISSION_GRANTED) {
                    // Permission is granted, open gallery
                    openGallery(context, getImageLauncher)
                } else {
                    // Request permission
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            },
            modifier = Modifier
                .padding(vertical = 16.dp)
                .background(brush = gradient2, shape = RoundedCornerShape(9.dp))
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Select an Image")
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
                .padding(all = 16.dp)
                .align(Alignment.CenterHorizontally)
        ) {

            ElevatedButton(
                onClick = {
                    bitmap?.let { bmp ->
                        aiViewModel.sendPrompt(bmp, prompt)
                    }
                },
                enabled = prompt.isNotEmpty() && bitmap != null,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .width(76.dp),
                colors = ButtonColors(
                    disabledContainerColor = Color.Red,
                    containerColor = Color.Blue,
                    contentColor = Color.White,
                    disabledContentColor = Color.White
                ),
                border = BorderStroke(2.dp, Color.White)
            ) {
                Text(text = stringResource(R.string.action_go))
            }
        }

        if (uiState is UiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            var textColor = Color.Black
            if (uiState is UiState.Error) {
                textColor = MaterialTheme.colorScheme.error
                result = (uiState as UiState.Error).errorMessage
            } else if (uiState is UiState.Success) {
                textColor = MaterialTheme.colorScheme.onSurface
                result = (uiState as UiState.Success).outputText
            }
            val scrollState = rememberScrollState()
            Text(
                text = formatText(result),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 0.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                color = Color.Black
            )
        }
    }
    }

// Helper function to open the gallery
private fun openGallery(context: Context, getImageLauncher: ActivityResultLauncher<String>) {
    getImageLauncher.launch("image/*")
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

