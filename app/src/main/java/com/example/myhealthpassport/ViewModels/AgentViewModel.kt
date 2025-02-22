package com.example.myhealthpassport.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myhealthpassport.Agent.AgentInstance
import com.example.myhealthpassport.Agent.MistralMessage
import com.example.myhealthpassport.Agent.MistralRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AgentViewModel : ViewModel() {

    private val mistralAgentApi = AgentInstance.mistralAgentApi

    // LiveData to hold the agent response
    private val _agentResponse = MutableLiveData<Result<String>>()
    val agentResponse: LiveData<Result<String>> = _agentResponse

    // Function to send query to the agent and update LiveData
    fun sendQueryToAgent(query: String) {
        viewModelScope.launch {
            try {
                // Create the request body
                val messages = listOf(MistralMessage(role = "user", content = query))
                val request = MistralRequest(agent_id = "ag:54631716:20240929:untitled-agent:1c20a87a", messages = messages)

                // Make the API call
                val response = withContext(Dispatchers.IO) {
                    mistralAgentApi.agentCompletion("Bearer 5WayNMBMumbUkfrDh3yoJDO9mdtn8niM", request).execute()
                }

                // Handle the API response
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val result = responseBody?.choices?.firstOrNull()?.message?.content
                    _agentResponse.postValue(Result.success(result ?: "No content")) // Update LiveData
                } else {
                    val errorResponse = response.errorBody()?.string() ?: "Unknown error"
                    _agentResponse.postValue(Result.failure(Exception(errorResponse))) // Update LiveData with error
                }
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string() ?: "HTTP exception occurred"
                _agentResponse.postValue(Result.failure(Exception(errorMessage))) // Handle HTTP error
            } catch (e: Exception) {
                _agentResponse.postValue(Result.failure(e)) // Handle other exceptions
            }
        }
    }
}
