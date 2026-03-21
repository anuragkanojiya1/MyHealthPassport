package com.example.myhealthpassport.domain.mistralModel

data class MistralRequest(
    val agent_id: String,
    val messages: List<MistralMessage>
)
