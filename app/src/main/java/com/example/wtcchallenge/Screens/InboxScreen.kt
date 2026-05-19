package com.example.wtcchallenge.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wtcchallenge.composables.ChatBottomBar
import com.example.wtcchallenge.composables.SupportMessage
import com.example.wtcchallenge.composables.UserMessage
import com.example.wtcchallenge.model.Client
import com.example.wtcchallenge.model.Message
import com.example.wtcchallenge.network.RetrofitInstance
import com.example.wtcchallenge.network.SessionManager
import com.example.wtcchallenge.network.dto.MessageRequestDto
import com.example.wtcchallenge.ui.theme.WTCChallengeTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxScreen(
    customerId: String,
    onBack: () -> Unit,
    onChatClick: (String) -> Unit
) {
    var client by remember { mutableStateOf<Client?>(null) }
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }
    var conversationId by remember { mutableStateOf<String?>(null) }
    var messageText by remember { mutableStateOf(TextFieldValue("")) }
    var isLoading by remember { mutableStateOf(true) }
    var isSending by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val darkBackground = Color(0xFF121417)

    LaunchedEffect(customerId) {
        isLoading = true
        errorMessage = null
        try {
            client = RetrofitInstance.api.getClientById(customerId)
            messages = RetrofitInstance.api.getInboxMessages(customerId)
            conversationId = RetrofitInstance.api.getInboxConversations(customerId)
                .firstOrNull()?.id
            if (messages.isNotEmpty()) listState.scrollToItem(messages.lastIndex)
        } catch (e: Exception) {
            errorMessage = "Erro ao carregar inbox: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    fun enviarMensagem() {
        val text = messageText.text.trim()
        if (text.isBlank() || isSending) return
        val operatorId = SessionManager.operatorId ?: run {
            errorMessage = "Sessão expirada."
            return
        }
        scope.launch {
            isSending = true
            try {
                val convId = conversationId ?: run {
                    val novaConversa = RetrofitInstance.api.getOrCreateConversation(
                        mapOf("clientId" to customerId, "operatorId" to operatorId)
                    )
                    conversationId = novaConversa.id
                    novaConversa.id
                }

                val sent = RetrofitInstance.api.sendMessage(
                    convId,
                    MessageRequestDto(
                        senderId = operatorId,
                        senderType = "OPERATOR",
                        content = text
                    )
                )
                messages = messages + sent
                messageText = TextFieldValue("")
                listState.scrollToItem(messages.lastIndex)
            } catch (e: Exception) {
                errorMessage = "Erro ao enviar: ${e.message}"
            } finally {
                isSending = false
            }
        }
    }

    Scaffold(
        containerColor = darkBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column {
                        Text(
                            text = client?.nome ?: "Cliente",
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        if (!client?.ramo.isNullOrBlank()) {
                            Text(
                                text = client!!.ramo,
                                color = Color(0xFF9EABBA),
                                fontSize = 12.sp
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1C1F24),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
                messages.isEmpty() -> Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Nenhuma mensagem ainda. Envie a primeira!",
                        color = Color(0xFF9EABBA),
                        fontSize = 14.sp
                    )
                }
                else -> LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(messages) { msg ->
                        if (msg.senderType == "OPERATOR") {
                            UserMessage(msg.content ?: "")
                        } else {
                            SupportMessage(msg.content ?: "")
                        }
                    }
                }
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            ChatBottomBar(
                messageText = messageText,
                onMessageChange = { messageText = it },
                onSendClick = { enviarMensagem() }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InboxScreenPreview() {
    WTCChallengeTheme {
        InboxScreen(customerId = "preview-id", onBack = {}, onChatClick = {})
    }
}
