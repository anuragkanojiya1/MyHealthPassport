package com.example.myhealthpassport.Health

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myhealthpassport.ViewModels.AiViewModel
import com.example.openaiapichatbot.response.MessageRequest
import com.example.openaiapichatbot.network.NetworkResponse
import com.example.openaiapichatbot.response.Message
import com.example.myhealthpassport.ViewModels.ChatViewModel

data class ChatMessage(
    val userMessage: String,
    val assistantResponse: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, viewModel: ChatViewModel) {
    var message by remember { mutableStateOf("") }
    var messageText by remember { mutableStateOf("") }
    var responseText by remember { mutableStateOf("") }

    var chatHistory by remember { mutableStateOf(listOf<ChatMessage>()) }

    // Observe the response from the ViewModel
    val response = viewModel.anthropicResult.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .padding(top = 20.dp, bottom = 92.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Start,
        ) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back_button")
            }
        }
        Column(modifier = Modifier.padding(8.dp)) {
        chatHistory.forEach { chatMessages ->
            Chats(chatMessages = chatMessages)
            }
        }
    }

    Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 36.dp)
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {

            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                label = { Text("Enter your message") },
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .padding(end = 8.dp)
                    .weight(0.7f)
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),

                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        containerColor = Color.Transparent
                    ),
            )

            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.sendMessage(
                        MessageRequest(
                            model = "claude-3-5-sonnet-20240620", // Specify your model here
                            max_tokens = 50, // Adjust as needed
                            messages = listOf(
                                Message(
                                    role = "user",
                                    content = messageText + """"You are a compassionate and knowledgeable medical therapist and psychiatrist.
                                        | Your goal is to provide patients with a calm, reassuring, and supportive environment where they feel heard and understood.
                                        | You offer thoughtful and professional guidance on mental health concerns, stress management, and emotional well-being.
                                        |  When interacting with patients, you use a soothing tone, ask gentle but insightful questions, and offer practical advice to help them navigate their feelings.
                                        |  You maintain a non-judgmental stance, encourage positive thinking, and provide strategies for coping with anxiety, depression, and other mental health challenges."""".trimMargin()

                                )
                            )
                        )
                    )
                    message = messageText
                    messageText = ""
                },
                modifier = Modifier
                    .weight(0.25f)
                    .padding(bottom = 4.dp)
                    .background(Color.Black, RoundedCornerShape(4.dp)),

                    containerColor = Color.LightGray
            ) {
                Text("Send")
            }
        }

    // Handle UI changes based on the response
    LaunchedEffect(response.value) {
        when (val result = response.value) {
            is NetworkResponse.Success -> {
                responseText = result.data.content[0].text
                chatHistory = chatHistory + ChatMessage(message, responseText)
            }
            is NetworkResponse.Error -> {
                responseText = result.message
                chatHistory = chatHistory + ChatMessage(message, responseText)
            }
            NetworkResponse.Loading -> {
                responseText = "Loading..."
            }
            null -> {}
        }
    }
}

@Composable
fun Chats(chatMessages : ChatMessage){
    Column(modifier = Modifier
        .padding(12.dp)
        .padding(bottom = 20.dp)
        ) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
            .background(color = Color.DarkGray, shape = RoundedCornerShape(4.dp))
        ) {
            Text(
                text = "User: ${chatMessages.userMessage}",
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(8.dp)
                )
        }
        Text(text = "Assistant: ${chatMessages.assistantResponse}",
            fontSize = 20.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.W400,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .padding(top = 16.dp)
                .wrapContentSize(Alignment.TopStart)
            )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview(){
    ChatScreen(navController = rememberNavController(), viewModel = ChatViewModel())
}