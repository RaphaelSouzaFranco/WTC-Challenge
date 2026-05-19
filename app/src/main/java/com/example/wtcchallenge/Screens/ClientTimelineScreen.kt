package com.example.wtcchallenge.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.example.wtcchallenge.ui.theme.WTCChallengeTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wtcchallenge.model.Client
import com.example.wtcchallenge.model.TimelineEvent
import com.example.wtcchallenge.network.RetrofitInstance
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientTimelineScreen(clientId: String, onBack: () -> Unit) {
    var client by remember { mutableStateOf<Client?>(null) }
    var events by remember { mutableStateOf<List<TimelineEvent>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val darkBackground = Color(0xFF121417)

    LaunchedEffect(clientId) {
        scope.launch {
            isLoading = true
            try {
                client = RetrofitInstance.api.getClientById(clientId)
                events = RetrofitInstance.api.getClientTimeline(clientId)
            } catch (e: Exception) {
                errorMessage = "Erro ao carregar timeline: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        containerColor = darkBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        client?.nome ?: "Timeline",
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
            client?.let { c ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF293038)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(c.nome, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("${c.ramo} | ${c.status}", color = Color(0xFF9EABBA), fontSize = 13.sp)
                            Text("Score: ${c.score}", color = Color(0xFF1E88E5), fontSize = 13.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text("Historico de Interacoes", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            when {
                isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
                errorMessage != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(errorMessage!!, color = Color.Red)
                }
                events.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhum evento registrado", color = Color(0xFF9EABBA))
                }
                else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(events) { event ->
                        TimelineEventItem(event)
                    }
                }
            }
        }
    }
}

@Composable
fun TimelineEventItem(event: TimelineEvent) {
    val color = when {
        event.type.contains("CREATED") -> Color(0xFF4CAF50)
        event.type.contains("CONVERSATION") -> Color(0xFF1E88E5)
        event.type.contains("OPERATOR") -> Color(0xFFFFA726)
        event.type.contains("CLIENT") -> Color(0xFF9C27B0)
        else -> Color(0xFF9EABBA)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(24.dp)) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(40.dp)
                    .background(Color(0xFF293038))
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = event.type.replace("_", " "),
                color = color,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
            Text(
                text = event.description ?: "",
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 2
            )
            event.timestamp?.let { ts ->
                Text(
                    text = ts.substringBefore("T").ifBlank { ts.take(19) },
                    color = Color(0xFF9EABBA),
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClientTimelineScreenPreview() {
    WTCChallengeTheme {
        Surface(color = Color(0xFF0D0D0D)) {
            ClientTimelineScreen(clientId = String(), onBack = {})
        }
    }
}
