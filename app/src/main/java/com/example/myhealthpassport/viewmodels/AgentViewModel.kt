package com.example.myhealthpassport.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myhealthpassport.data.datastore.GetApiKeyUseCase
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgentViewModel @Inject constructor(
    private val getApiKeyUseCase: GetApiKeyUseCase
) : ViewModel() {

    private val _agentResponse = MutableLiveData<Result<String>>()
    val agentResponse: LiveData<Result<String>> = _agentResponse

    private suspend fun getGenerativeModel(): GenerativeModel {
        // Retrieve the Gemini API key stored in the app's DataStore
        val apiKey = getApiKeyUseCase().first() ?: ""
        return GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = apiKey,
            systemInstruction = content {
                text("You are a specialized health and diet agent for MyHealth Passport. " +
                     "Your goal is to provide personalized diet plans and exercise recommendations based on the user's medical profile. " +
                     "Be concise, professional, and health-focused.")
            }
        )
    }

    fun sendQueryToAgent(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val model = getGenerativeModel()
                val response = model.generateContent(
                    content { text(query) }
                )
                
                val result = response.text ?: "No content"
                _agentResponse.postValue(Result.success(result))
            } catch (e: Exception) {
                _agentResponse.postValue(Result.failure(e))
            }
        }
    }
}
