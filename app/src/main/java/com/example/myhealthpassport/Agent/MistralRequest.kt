package com.example.myhealthpassport.Agent

data class MistralRequest(
    val agent_id: String,
    val messages: List<MistralMessage>
)
