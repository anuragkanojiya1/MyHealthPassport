import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myhealthpassport.ViewModels.FileViewModel
import com.example.myhealthpassport.environmentId
import com.example.myhealthpassport.projectId
import java.io.File
import java.io.FileOutputStream

@Composable
fun FileUploadDownloadScreen(navController: NavController, viewModel: FileViewModel) {
    val context = LocalContext.current

    val uploadState by viewModel.uploadState.observeAsState()

    val filePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val fileName = getFileNameFromUri(context, uri) // Fetch the file name
                val file = File(context.cacheDir, fileName) // Use the original file name

                val inputStream = context.contentResolver.openInputStream(uri)
                val outputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)

                viewModel.uploadFile(file, environmentId, projectId)
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Upload/Download Files",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 24.sp
        )

        OutlinedButton(onClick = { filePickerLauncher.launch("*/*") }) {
            Text(text = "Upload File")
        }

        uploadState?.let { Text(text = it, color = Color.Green) }

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .paddingFromBaseline(top = 10.dp, bottom = 10.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Box {
                com.example.myhealthpassport.SignInSignUp.AnimatedCloud(modifier = Modifier
                    .size(500.dp, 400.dp)
                    .align(Alignment.Center)
                    // .scale(scaleX = 1.3f, scaleY = 1.6f)
                )
            }
        }

//        Button(onClick = {
//            // Call download function with the filename
//            viewModel.downloadFile("filename", environmentId, projectId)
//        }) {
//            Text(text = "Download File")
//        }

        val downloadUrl by viewModel.downloadUrl.observeAsState()
        val downloadState by viewModel.downloadState.observeAsState()

        var fileName by remember { mutableStateOf("") }
        val environmentId by remember { mutableStateOf(environmentId) } // Replace with actual environment ID
        val projectId by remember { mutableStateOf(projectId) } // Replace with actual project ID

        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = fileName,
                onValueChange = { fileName = it },
                label = { Text("Enter File Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    viewModel.fetchDownloadUrl(fileName, environmentId, projectId)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Get Download Link")
            }
            Spacer(modifier = Modifier.height(16.dp))

            downloadState?.let {
                Text(text = it)
            }

            downloadUrl?.let {
                Text(text = "Download URL: $it")
            }
            if(downloadUrl?.isNotBlank() == true) {
                Button(onClick = {
                    if (downloadUrl?.isNotEmpty() == true) {
                        downloadFile(downloadUrl!!, fileName, context)
                    }
                }) {
                    Text(text = "Download File")
                }
            }
        }
    }
}

// Function to get the file name from the Uri
fun getFileNameFromUri(context: Context, uri: Uri): String {
    var fileName = "unknown_file"
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (nameIndex >= 0) {
                fileName = it.getString(nameIndex)
            }
        }
    }
    return fileName
}

fun downloadFile(downloadUrl: String, fileName: String, context: Context) {
    try {
        val request = DownloadManager.Request(Uri.parse(downloadUrl))
            .setTitle(fileName)
            .setDescription("Downloading $fileName")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            .setMimeType(getMimeType(downloadUrl)) // Set correct MIME type

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)

        Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Failed to download file: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

// Function to get MIME type based on the URL (or file extension)
fun getMimeType(url: String): String? {
    val extension = MimeTypeMap.getFileExtensionFromUrl(url)
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
}


@Preview(showBackground = true)
@Composable
fun MyApp() {
    val fileViewModel: FileViewModel = viewModel()

    FileUploadDownloadScreen(navController = rememberNavController(), viewModel = fileViewModel)
}