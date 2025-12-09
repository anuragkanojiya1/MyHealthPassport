package com.example.myhealthpassport.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myhealthpassport.AGENT_ID
import com.example.myhealthpassport.Agent.AgentInstance
import com.example.myhealthpassport.Agent.MistralMessage
import com.example.myhealthpassport.Agent.MistralRequest
import com.example.myhealthpassport.MISTRAL_API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AgentViewModel : ViewModel() {

    private val mistralAgentApi = AgentInstance.mistralAgentApi

    private val _agentResponse = MutableLiveData<Result<String>>()
    val agentResponse: LiveData<Result<String>> = _agentResponse

    fun sendQueryToAgent(query: String) {
        viewModelScope.launch {
            try {

                val messages = listOf(MistralMessage(role = "user", content = query))
                val request = MistralRequest(agent_id = AGENT_ID, messages = messages)


                val response = withContext(Dispatchers.IO) {
                    mistralAgentApi.agentCompletion(MISTRAL_API_KEY, request).execute()
                }


                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val result = responseBody?.choices?.firstOrNull()?.message?.content
                    _agentResponse.postValue(Result.success(result ?: "No content"))
                } else {
                    val errorResponse = response.errorBody()?.string() ?: "Unknown error"
                    _agentResponse.postValue(Result.failure(Exception(errorResponse)))
                }
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string() ?: "HTTP exception occurred"
                _agentResponse.postValue(Result.failure(Exception(errorMessage)))
            } catch (e: Exception) {
                _agentResponse.postValue(Result.failure(e))
            }
        }
    }
}
