package com.example.wtcchallenge.Screens

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wtcchallenge.composables.BottomNavigationBar
import com.example.wtcchallenge.composables.Client
import com.example.wtcchallenge.composables.ConversationItem
import com.example.wtcchallenge.composables.SelectableButton
import com.example.wtcchallenge.network.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun MessagesScreen(
    onMessagesClick: () -> Unit,
    onCampaignClick: () -> Unit,
    onClientClick: () -> Unit,
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit  // ADICIONADO: callback para navegar ao chat
) {
    var selectedFilter by remember { mutableStateOf("Todos") }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    // MUDOU: Estados para API
    var clientes by remember { mutableStateOf<List<Client>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    // ADICIONADO: Carregar clientes da API
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                isLoading = true
                errorMessage = null
                val lista = RetrofitInstance.api.getClients()
                clientes = lista
            } catch (e: Exception) {
                errorMessage = "Erro ao carregar conversas: ${e.message}"
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    // MUDOU: Filtrar por nome e ramo
    val filteredList = clientes.filter {
        it.nome.contains(searchQuery.text, ignoreCase = true) ||
                it.ramo.contains(searchQuery.text, ignoreCase = true)
    }

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
                Text(
                    text = "Conversas",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { /* ação futura */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SelectableButton("Todos", selectedFilter) { selectedFilter = "Todos" }
                SelectableButton("Clientes", selectedFilter) { selectedFilter = "Clientes" }
                SelectableButton("Grupos", selectedFilter) { selectedFilter = "Grupos" }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // MUDOU: Adiciona estados de Loading e Error
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = errorMessage ?: "Erro desconhecido",
                                color = Color.Red
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {
                                scope.launch {
                                    try {
                                        isLoading = true
                                        errorMessage = null
                                        val lista = RetrofitInstance.api.getClients()
                                        clientes = lista
                                    } catch (e: Exception) {
                                        errorMessage = "Erro: ${e.message}"
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }) {
                                Text("Tentar Novamente")
                            }
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredList) { cliente ->  // MUDOU: agora itera sobre Client
                            ConversationItem(
                                cliente = cliente,
                                onClick = onChatClick  // ADICIONADO: navega ao clicar
                            )
                        }
                    }
                }
            }
        }
    }
}