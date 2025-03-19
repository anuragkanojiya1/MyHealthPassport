package com.example.myhealthpassport.aicompanion

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.codewizard.aicompanion.MessageModel
import com.example.myhealthpassport.MainActivity
import kotlinx.coroutines.launch

@Composable
fun ChatPage(navController: NavController, context: Context, viewModel: ChatViewModel) {

    Column(modifier = Modifier.fillMaxSize()
        .background(Color.White)) {

        MessageList(modifier = Modifier.weight(1f), messageList = viewModel.messageList)

           Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FloatingActionButton(onClick = { viewModel.clearChat() },
                containerColor = Color(0xFFE9EFF9),
                modifier = Modifier.padding(top = 8.dp)) {
                Icon(imageVector = Icons.Default.Delete,
                    contentDescription = "Clear Chat",
                    tint = Color.Black)
            }
            MessageInput(context, onMessageSend = { viewModel.sendMessage(it) })
        }

    }
}

@Composable
fun MessageInput(context: Context, onMessageSend: (String) -> Unit) {
    val speechContext = context as MainActivity
    var message by remember { mutableStateOf("") }

    LaunchedEffect(speechContext.speechInput.value) {
        if (speechContext.speechInput.value.isNotBlank()) {
            message = speechContext.speechInput.value
            speechContext.speechInput.value = ""
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Type a message...", color = Color.Gray) }
        )

        IconButton(onClick = {
            if (message.isNotBlank()) {
                onMessageSend(message)
                message = ""
            }
        }) {
            Icon(imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = Color.Black)
        }

        FloatingActionButton(
            onClick = { speechContext.askSpeechInput(context) },
            modifier = Modifier.padding(start = 8.dp),
            containerColor = Color(0xFFE9EFF9)
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Voice Input",
                tint = Color.Black
            )
        }
    }
}

@Composable
fun MessageList(modifier: Modifier, messageList: List<MessageModel>) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(messageList.size) {
        if (messageList.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(messageList.size - 1)
            }
        }
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        state = listState,
        reverseLayout = false
    ) {
        items(messageList) { message ->
            MessageRow(message)
        }
    }
}

@Composable
fun MessageRow(message: MessageModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.role == "user") Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (message.role == "user") Color(0xFF007AFF) else Color(0xFFECECEC))
                .padding(12.dp)
                .widthIn(max = 250.dp)
        ) {
            Text(
                text = message.message,
                color = if (message.role == "user") Color.White else Color.Black,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
