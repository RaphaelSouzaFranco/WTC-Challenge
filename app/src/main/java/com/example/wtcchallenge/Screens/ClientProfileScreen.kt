package com.example.wtcchallenge.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wtcchallenge.model.Client
import com.example.wtcchallenge.network.RetrofitInstance
import com.example.wtcchallenge.ui.theme.WTCChallengeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientProfileScreen(clientId: String, onBack: () -> Unit) {
    var client by remember { mutableStateOf<Client?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val darkBackground = Color(0xFF121417)
    val cardColor = Color(0xFF293038)
    val accentBlue = Color(0xFF1E88E5)
    val secondaryText = Color(0xFF9EABBA)

    LaunchedEffect(clientId) {
        try {
            client = RetrofitInstance.api.getClientById(clientId)
        } catch (e: Exception) {
            errorMessage = "Erro ao carregar perfil: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        containerColor = darkBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Perfil do Cliente", fontWeight = FontWeight.SemiBold, maxLines = 1) },
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
        when {
            isLoading -> Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = Color.White) }

            errorMessage != null -> Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) { Text(errorMessage!!, color = Color.Red) }

            client != null -> {
                val c = client!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(cardColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = secondaryText,
                            modifier = Modifier.size(60.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(c.nome, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)

                    // Status badge
                    Spacer(modifier = Modifier.height(8.dp))
                    val statusColor = if (c.status.equals("Ativo", ignoreCase = true))
                        Color(0xFF4CAF50) else Color(0xFFEF5350)
                    Surface(
                        color = statusColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = c.status.ifBlank { "Sem status" },
                            color = statusColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Score card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ScoreItem(label = "Score", value = c.score.toString(), color = accentBlue)
                            ScoreItem(label = "Ramo", value = c.ramo.ifBlank { "-" }, color = Color(0xFFFFA726))
                            ScoreItem(label = "Tags", value = c.tags.size.toString(), color = Color(0xFF9C27B0))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Info card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Informacoes",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            ClientInfoRow(
                                icon = Icons.Default.Phone,
                                label = "Telefone",
                                value = c.numero.ifBlank { "Nao informado" }
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            ClientInfoRow(
                                icon = Icons.Default.Star,
                                label = "Ramo de Atuacao",
                                value = c.ramo.ifBlank { "Nao informado" }
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            ClientInfoRow(
                                icon = Icons.Default.DateRange,
                                label = "Cadastro",
                                value = c.createdAt?.substringBefore("T") ?: "Nao disponivel"
                            )
                        }
                    }

                    // Tags
                    if (c.tags.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "Tags",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                FlowRowTags(tags = c.tags)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun ScoreItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text(label, color = Color(0xFF9EABBA), fontSize = 12.sp)
    }
}

@Composable
private fun ClientInfoRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color(0xFF1E88E5), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, color = Color(0xFF9EABBA), fontSize = 11.sp)
            Text(value, color = Color.White, fontSize = 14.sp)
        }
    }
}

@Composable
private fun FlowRowTags(tags: List<String>) {
    Column {
        val chunked = tags.chunked(4)
        chunked.forEach { chunk ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                chunk.forEach { tag ->
                    Surface(
                        color = Color(0xFF1E88E5).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = tag,
                            color = Color(0xFF1E88E5),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClientProfileScreenPreview() {
    WTCChallengeTheme {
        Surface(color = Color(0xFF0D0D0D)) {
            ClientProfileScreen(clientId = "", onBack = {})
        }
    }
}
