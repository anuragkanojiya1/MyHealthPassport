package com.example.myhealthpassport.aicompanion

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arwebmodel.aicompanion.ChatDatabase
import com.example.arwebmodel.aicompanion.MessageEntity
import com.example.codewizard.aicompanion.MessageModel
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.launch

class ChatViewModel(private val context: Context) : ViewModel() {

    private val database = ChatDatabase.getDatabase(context)
    private val messageDao = database.messageDao()

    val messageList = mutableStateListOf<MessageModel>()

    private var chatSession: Chat? = null

    val generativeModel = GenerativeModel(
        modelName = "tunedModels/aisymptomchecker-g1c5995817ew",
        apiKey = "AIzaSyCQRpU5vrnBKlm7b3c8XNzQthhwx-S1_og",
        generationConfig = generationConfig {
            temperature = 0.2f         // Low randomness for accuracy
            topK = 20                  // Limits token selection for better control
            topP = 0.9f                // Ensures coherence while allowing some diversity
            maxOutputTokens = 1024
            responseMimeType = "text/plain"
        },
    )

    init {
        startChat()
        loadMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            val messages = messageDao.getAllMessages()
            messageList.clear()
            messageList.addAll(messages.map { MessageModel(it.message, it.role) })
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            messageDao.deleteAllMessages()  // Clear database
            messageList.clear()             // Clear UI list
            startChat()                     // Restart chat session
        }
    }


    // Initialize chat session with history from Room database
    private fun startChat() {
        viewModelScope.launch {
            // Fetch chat history from Room database
            val messages = messageDao.getAllMessages()  // Get all messages from Room

            // Prepare the history by converting Room messages to content format
            val chatHistory = messages.map {
                if (it.role == "user") {
                    content(role = "user") { text(it.message) }
                } else {
                    content(role = "model") { text(it.message) }
                }
            }

            // Start the chat session with the fetched history
            chatSession = generativeModel.startChat(
                history = chatHistory.ifEmpty {
                    // If history is empty, start with default initial message
                    listOf(
                        content(role = "user") { text("Hello, I am user.") },
                        content(role = "model") { text("Great to meet you. What would you like to know?") }
                    )
                }
            )
        }
    }

    fun sendMessage(question: String) {
        viewModelScope.launch {
            try {
                // Add user message to database and UI
                val userMessage = MessageEntity(message = question, role = "user")
                messageDao.insertMessage(userMessage)
                messageList.add(MessageModel(question, "user"))

                if (chatSession == null) {
                    Log.e("ChatViewModel", "Chat session is not initialized.")
                    return@launch
                }

                // Collect streamed response in chunks and accumulate text
                val responseBuilder = StringBuilder()

                chatSession?.sendMessageStream(question)?.collect { chunk ->
                    responseBuilder.append(chunk.text)

                    // Log each chunk (for debugging)
                    Log.d("ChatViewModel", "Received chunk: ${chunk.text}")
                }

                // Convert accumulated text to a full response
                val fullResponse = responseBuilder.toString().trim()

                // Store in database and UI **only after full response is received**
                if (fullResponse.isNotEmpty()) {
                    val modelMessage = MessageEntity(message = fullResponse, role = "model")
                    messageDao.insertMessage(modelMessage)
                    messageList.add(MessageModel(fullResponse, "model"))
                }

            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error sending message: ${e.localizedMessage}", e)
            }
        }
    }


//    fun sendMessage(question: String) {
//        viewModelScope.launch {
//            try {
//                // Add user message to database
//                val userMessage = MessageEntity(message = question, role = "user")
//                messageDao.insertMessage(userMessage)
//                messageList.add(MessageModel(question, "user"))
//
//                if (chatSession == null) {
//                    Log.e("ChatViewModel", "Chat session is not initialized.")
//                }
//
//                // Send the message and get the response (from Gemini API)
//                val response = chatSession?.sendMessageStream(question)?.collect { chunk ->
//                    val modelMessage = MessageEntity(message = chunk.text.toString(), role = "model")
//                    messageDao.insertMessage(modelMessage)
//                    messageList.add(MessageModel(chunk.text.toString(), "model"))
//                }
//                if (response == null) {
//                    Log.e("ChatViewModel", "Received null response from the API")
//                }
//
//            } catch (e: Exception) {
//                Log.e("ChatViewModel", "Error sending message: ${e.localizedMessage}", e)
//            }
//        }
//    }


}
