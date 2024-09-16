package com.example.myhealthpassport.Anthropic

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private fun getInstance(): Retrofit{
            return Retrofit.Builder()
            .baseUrl("https://api.anthropic.com/v1/messages/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val anthropicApi : AnthropicApi = getInstance().create(AnthropicApi::class.java)
}
