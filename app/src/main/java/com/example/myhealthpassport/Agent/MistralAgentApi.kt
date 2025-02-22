package com.example.myhealthpassport.Agent

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface MistralAgentApi {

    @POST("v1/agents/completions")
    @Headers("Content-Type: application/json", "Accept: application/json")
    fun agentCompletion(
        @Header("Authorization") agentApiKey: String,
        @Body request: MistralRequest
    ): Call<MistralResponse>
}
