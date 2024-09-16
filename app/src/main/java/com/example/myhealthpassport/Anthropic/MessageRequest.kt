package com.example.myhealthpassport.Anthropic

data class MessageRequest(
    val model: String,
    val max_tokens: Int,
    val messages: List<Message>
)