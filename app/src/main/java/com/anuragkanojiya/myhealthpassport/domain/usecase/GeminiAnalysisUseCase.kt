package com.anuragkanojiya.myhealthpassport.domain.usecase

import android.graphics.Bitmap
import com.anuragkanojiya.myhealthpassport.data.datastore.GetApiKeyUseCase
import com.anuragkanojiya.myhealthpassport.domain.model.UserHealthData
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import javax.inject.Inject

class GeminiAnalysisUseCase @Inject constructor(
    private val getApiKeyUseCase: GetApiKeyUseCase
) {

    private suspend fun getGenerativeModel(): GenerativeModel {
        val apiKey = getApiKeyUseCase().first() ?: ""
        return GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = apiKey
        )
    }

    suspend fun analyzeData(prompt: String): Result<String> = try {
        val model = getGenerativeModel()
        val response = model.generateContent(
            content { text(prompt) }
        )
        val text = response.text
        if (text != null) Result.success(text) else Result.failure(Exception("Empty AI response"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun analyzeImage(bitmap: Bitmap, prompt: String): Result<String> = try {
        val model = getGenerativeModel()
        val response = model.generateContent(
            content {
                image(bitmap)
                text(prompt)
            }
        )
        val text = response.text
        if (text != null) Result.success(text) else Result.failure(Exception("Empty AI response"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun extractMedicalReport(jsonResponse: String): UserHealthData {
        val cleanJson = cleanJsonResponse(jsonResponse)
        val jsonObject = JSONObject(cleanJson)
        return UserHealthData(
            name = jsonObject.optString("name", "Unknown"),
            bloodGroup = jsonObject.optString("bloodGroup", "Not Provided"),
            age = parseSafeInt(jsonObject, "age", 0),
            systolicBP = parseSafeInt(jsonObject, "systolicBP", 130),
            diastolicBP = parseSafeInt(jsonObject, "diastolicBP", 85),
            bloodSugarLevel = parseSafeInt(jsonObject, "bloodSugarLevel", 120),
            gender = jsonObject.optString("gender", "Not Provided"),
            healthCondition = jsonObject.optString("healthCondition", "No Condition Recorded"),
            emergencyPhoneNumber = jsonObject.optString("emergencyPhoneNumber", "0000000000"),
            address = jsonObject.optString("address", "No Address Provided"),
            allergies = jsonObject.optString("allergies", "None"),
            medications = jsonObject.optString("medications", "No Medications"),
            weight = parseSafeFloat(jsonObject, "weight", 0.0f),
            height = parseSafeFloat(jsonObject, "height", 0.0f)
        )
    }

    private fun cleanJsonResponse(response: String): String {
        val trimmed = response.trim()
        val startIndex = trimmed.indexOf('{')
        val endIndex = trimmed.lastIndexOf('}')
        return if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            trimmed.substring(startIndex, endIndex + 1)
        } else {
            trimmed
        }
    }

    private fun parseSafeInt(jsonObject: JSONObject, key: String, defaultValue: Int): Int {
        val value = jsonObject.opt(key) ?: return defaultValue
        return when (value) {
            is Number -> value.toInt()
            is String -> value.filter { it.isDigit() || it == '-' }.toIntOrNull() ?: defaultValue
            else -> defaultValue
        }
    }

    private fun parseSafeFloat(jsonObject: JSONObject, key: String, defaultValue: Float): Float {
        val value = jsonObject.opt(key) ?: return defaultValue
        return when (value) {
            is Number -> value.toFloat()
            is String -> value.filter { it.isDigit() || it == '.' || it == '-' }.toFloatOrNull() ?: defaultValue
            else -> defaultValue
        }
    }
}
