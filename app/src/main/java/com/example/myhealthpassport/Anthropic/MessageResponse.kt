package com.example.myhealthpassport.Anthropic

data class MessageResponse(
    val content: List<Content>,
    val id: String,
    val model: String,
    val role: String,
    val stop_reason: String,
    val stop_sequence: Any,
    val type: String,
    val usage: Usage
)