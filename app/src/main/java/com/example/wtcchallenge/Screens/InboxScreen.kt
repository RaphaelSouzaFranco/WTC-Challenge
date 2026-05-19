package com.example.wtcchallenge.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wtcchallenge.composables.ConversationItem
import com.example.wtcchallenge.model.Client
import com.example.wtcchallenge.model.Conversation
import com.example.wtcchallenge.network.RetrofitInstance
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
    var conversations by remember { mutableStateOf<List<Conversation>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val darkBackground = Color(0xFF121417)

    fun loadData() {
        scope.launch {
            isLoading = true
            errorMessage = null
            try {
                client = RetrofitInstance.api.getClientById(customerId)
                conversations = RetrofitInstance.api.getInboxConversations(customerId)
            } catch (e: Exception) {
                errorMessage = "Erro ao carregar inbox: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(customerId) { loadData() }

    Scaffold(
        containerColor = darkBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Inbox - ${client?.nome ?: "Cliente"}",
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = darkBackground,
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
                .padding(horizontal = 16.dp)
        ) {
            when {
                isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
                errorMessage != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(errorMessage!!, color = Color.Red)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { loadData() }) { Text("Tentar Novamente") }
                    }
                }
                conversations.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Nenhuma conversa encontrada para este cliente",
                        color = Color(0xFF9EABBA),
                        fontSize = 16.sp
                    )
                }
                else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(conversations) { conversation ->
                        ConversationItem(
                            conversation = conversation,
                            onClick = { onChatClick(conversation.id) }
                        )
                        HorizontalDivider(
                            color = Color(0xFF293038),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun InboxScreenPreview() {
    WTCChallengeTheme {
        Surface(color = Color(0xFF0D0D0D)) {
            InboxScreen(customerId= String(), onBack={}, onChatClick = {})
        }
    }
}

