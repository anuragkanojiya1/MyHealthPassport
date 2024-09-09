package com.example.myhealthpassport.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myhealthpassport.ANTHROPIC_API_KEY
import com.example.openaiapichatbot.network.NetworkResponse
import com.example.myhealthpassport.network.RetrofitInstance
import com.example.openaiapichatbot.response.MessageRequest
import com.example.openaiapichatbot.response.MessageResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatViewModel: ViewModel() {

    private val anthropicApi = RetrofitInstance.anthropicApi
    private val _anthropicResult = MutableLiveData<NetworkResponse<MessageResponse>>()
    val anthropicResult : LiveData<NetworkResponse<MessageResponse>> = _anthropicResult

    fun sendMessage(messageRequest: MessageRequest) {
        _anthropicResult.value = NetworkResponse.Loading

        anthropicApi.chatCompletion(
            messageRequest,
            "$ANTHROPIC_API_KEY", // Use your API key
            version = "2023-06-01",
            contentType = "application/json"
        ).enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _anthropicResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _anthropicResult.value = NetworkResponse.Error("Failed to load data")
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                _anthropicResult.value = NetworkResponse.Error("Failed to load data")
            }
        })
    }

}