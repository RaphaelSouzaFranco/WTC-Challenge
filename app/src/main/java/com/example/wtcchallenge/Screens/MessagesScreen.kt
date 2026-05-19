package com.example.wtcchallenge.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wtcchallenge.composables.BottomNavigationBar
import com.example.wtcchallenge.composables.ConversationItem
import com.example.wtcchallenge.composables.SelectableButton
import com.example.wtcchallenge.model.Conversation
import com.example.wtcchallenge.network.RetrofitInstance
import com.example.wtcchallenge.network.SessionManager
import kotlinx.coroutines.launch

@Composable
fun MessagesScreen(
    onMessagesClick: () -> Unit,
    onCampaignClick: () -> Unit,
    onClientClick: () -> Unit,
    onProfileClick: () -> Unit,
    onChatClick: (conversationId: String) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Todos") }
    var conversations by remember { mutableStateOf<List<Conversation>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    fun loadConversations(filter: String) {
        scope.launch {
            isLoading = true
            errorMessage = null
            try {
                val operatorId = SessionManager.operatorId ?: return@launch
                conversations = RetrofitInstance.api.getConversations(operatorId, filter)
            } catch (e: Exception) {
                errorMessage = "Erro ao carregar conversas: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) { loadConversations(selectedFilter) }

    Scaffold(
        containerColor = Color(0xFF121417),
        bottomBar = {
            BottomNavigationBar(
                onMessagesClick = onMessagesClick,
                onCampaignClick = onCampaignClick,
                onClientClick = onClientClick,
                onProfileClick = onProfileClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Conversas", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                listOf("Todos", "Clientes", "Grupos").forEach { label ->
                    SelectableButton(label, selectedFilter) {
                        selectedFilter = label
                        loadConversations(label)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
                errorMessage != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(errorMessage!!, color = Color.Red)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { loadConversations(selectedFilter) }) { Text("Tentar Novamente") }
                    }
                }
                conversations.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhuma conversa encontrada.", color = Color(0xFF9EABBA))
                }
                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxSize()) {
                    items(conversations) { conv ->
                        ConversationItem(conversation = conv, onClick = { onChatClick(conv.id) })
                    }
                }
            }
        }
    }
}
