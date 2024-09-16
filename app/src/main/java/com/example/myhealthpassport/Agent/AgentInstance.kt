package com.example.myhealthpassport.Agent

import com.example.myhealthpassport.BASE_URL_MISTRAL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AgentInstance {

    private fun agentInstance(): Retrofit {

        // Create an OkHttpClient with custom timeout settings
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Read timeout
            .writeTimeout(30, TimeUnit.SECONDS)   // Write timeout
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.mistral.ai/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val mistralAgentApi: MistralAgentApi = agentInstance().create(MistralAgentApi::class.java)
}
