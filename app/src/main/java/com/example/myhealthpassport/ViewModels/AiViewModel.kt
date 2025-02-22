package com.example.myhealthpassport.ViewModels
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import com.example.myhealthpassport.Composables.UiState
import com.example.myhealthpassport.Composables.UserHealthData
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class AiViewModel: ViewModel(){

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)

    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash-001",
        apiKey = "AIzaSyCer8XVmetAX8AdWxg4x470ujEchgYlwYI"
    )
    fun extractMedicalReport(jsonResponse: String): UserHealthData {
        val jsonObject = JSONObject(jsonResponse)

        return UserHealthData(
            name = jsonObject.optString("name", "Unknown"),
            bloodGroup = jsonObject.optString("bloodGroup", "Not Provided"),
            age = jsonObject.optInt("age", 0),
            systolicBP = jsonObject.optInt("systolicBP", 130),
            diastolicBP = jsonObject.optInt("diastolicBP", 85),
            bloodSugarLevel = jsonObject.optInt("bloodSugarLevel", 120),
            gender = jsonObject.optString("gender", "Not Provided"),
            healthCondition = jsonObject.optString("healthCondition", "No Condition Recorded"),
            emergencyPhoneNumber = jsonObject.optString("emergencyPhoneNumber", "0000000000"),
            address = jsonObject.optString("address", "No Address Provided"),
            allergies = jsonObject.optString("allergies", "None"),
            medications = jsonObject.optString("medications", "No Medications"),
            weight = jsonObject.optDouble("weight", 0.0).toFloat(),
            height = jsonObject.optDouble("height", 0.0).toFloat()
        )
    }
    fun extractJsonObject(response: String): String {
        val startIndex = response.indexOf("{")
        val endIndex = response.lastIndexOf("}")

        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            val extractedJson = response.substring(startIndex, endIndex + 1).trim()
            return try {
                JSONObject(extractedJson)
                extractedJson
            } catch (e: Exception) {
                throw IllegalArgumentException("Invalid JSON format: ${e.localizedMessage}")
            }
        } else {
            throw IllegalArgumentException("No valid JSON found in response!")
        }
    }

    fun analyzeData(prompt: String, onResult: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content { text(prompt) }
                )
                response.text?.let { outputContent ->
                    onResult(outputContent)
                }
            } catch (e: Exception) {
                onResult("Error analyzing data: ${e.localizedMessage}")
            }
        }
    }

    fun sendPrompt(
        bitmap: Bitmap,
        prompt: String
    ) {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                            image(bitmap)
                            text(prompt)
                    }
                )
                response.text?.let { outputContent ->

                    _uiState.value = UiState.Success(outputContent)
                }
            } catch (e: Exception){
                _uiState.value = UiState.Error(e.localizedMessage ?: "")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AIScreen(){
    val ai = AiViewModel()
    val json = "```json\n" +
            "                                                                                                    {\n" +
            "                                                                                                      \"name\": \"Celeste Lim\",\n" +
            "                                                                                                      \"bloodGroup\": \"o\",\n" +
            "                                                                                                      \"age\": 7,\n" +
            "                                                                                                      \"gender\": \"Female\",\n" +
            "                                                                                                      \"healthCondition\": \"Gastroenteritis (Norovirus)\",\n" +
            "                                                                                                      \"emergencyPhoneNumber\": \"76787\",\n" +
            "                                                                                                      \"address\": \"ffgb\",\n" +
            "                                                                                                      \"allergies\": \"N.A.\",\n" +
            "                                                                                                      \"medications\": \"gfbvnbngg\",\n" +
            "                                                                                                      \"weight\": 34.6,\n" +
            "                                                                                                      \"height\": 32.4\n" +
            "                                                                                                    }\n" +
            "                                                                                                    ```".trimIndent()

    var x=""
    Column(modifier = Modifier.fillMaxSize()){
        Button(onClick = {
            x=ai.extractJsonObject(json)
        }){
            Text("bvbvb")
        }
        Button(onClick = {
            Log.d("Response:", x)
            ai.extractMedicalReport(x)
            Log.d("Response:", ai.extractMedicalReport(x).weight.toString()
            )
        }){
            Text("bvbvb")
        }
    }
}