package com.example.myhealthpassport.Anthropic

import com.example.myhealthpassport.ANTHROPIC_API_KEY
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AnthropicApi {

    @POST("v1/messages")
    fun chatCompletion(
        @Body messageRequest : MessageRequest,
        @Header("x-api-key") xApiKey : String = "$ANTHROPIC_API_KEY",
        @Header("anthropic-version")  version : String = "2023-06-01",
        @Header("content-type") contentType : String = "application/json"
        ) : Call<MessageResponse>
}