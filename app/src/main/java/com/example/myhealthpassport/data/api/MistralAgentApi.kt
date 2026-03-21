package com.example.myhealthpassport.data.api

import com.example.myhealthpassport.domain.mistralModel.MistralRequest
import com.example.myhealthpassport.domain.mistralModel.MistralResponse
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
