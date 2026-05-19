package com.example.wtcchallenge.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wtcchallenge.composables.BottomNavigationBar
import com.example.wtcchallenge.composables.ChatBottomBar
import com.example.wtcchallenge.composables.SupportMessage
import com.example.wtcchallenge.composables.TrackOrderButton
import com.example.wtcchallenge.composables.UserMessage
import com.example.wtcchallenge.model.Message
import com.example.wtcchallenge.network.RetrofitInstance
import com.example.wtcchallenge.network.SessionManager
import com.example.wtcchallenge.network.dto.MessageRequestDto
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(
    conversationId: String,
    onMessagesClick: () -> Unit,
    onCampaignClick: () -> Unit,
    onClientClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var messageText by remember { mutableStateOf(TextFieldValue("")) }
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isSending by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(conversationId) {
        try {
            val page = RetrofitInstance.api.getMessages(conversationId)
            messages = page.content
            if (messages.isNotEmpty()) listState.scrollToItem(messages.lastIndex)
        } catch (e: Exception) {
            // mantém lista vazia
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        containerColor = Color(0xFF121417),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Suporte", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("Customer Support", color = Color(0xFF9EABBA), fontSize = 12.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onMessagesClick() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1C1F24))
            )
        },
        bottomBar = {
            BottomNavigationBar(
                onMessagesClick = onMessagesClick,
                onCampaignClick = onCampaignClick,
                onClientClick = onClientClick,
                onProfileClick = onProfileClick
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when {
                isLoading -> Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
                else -> LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(messages) { msg ->
                        when {
                            msg.type == "DEEPLINK" -> TrackOrderButton()
                            msg.senderType == "OPERATOR" -> UserMessage(msg.content ?: "")
                            else -> SupportMessage(msg.content ?: "")
                        }
                    }
                }
            }

            ChatBottomBar(
                messageText = messageText,
                onMessageChange = { messageText = it },
                onSendClick = {
                    val text = messageText.text.trim()
                    if (text.isBlank() || isSending) return@ChatBottomBar
                    scope.launch {
                        isSending = true
                        try {
                            val operatorId = SessionManager.operatorId ?: return@launch
                            val sent = RetrofitInstance.api.sendMessage(
                                conversationId,
                                MessageRequestDto(senderId = operatorId, senderType = "OPERATOR", content = text)
                            )
                            messages = messages + sent
                            messageText = TextFieldValue("")
                            listState.scrollToItem(messages.lastIndex)
                        } catch (e: Exception) {
                            // falha silenciosa
                        } finally {
                            isSending = false
                        }
                    }
                }
            )
        }
    }
}
