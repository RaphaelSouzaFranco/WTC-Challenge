package com.example.wtcchallenge.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wtcchallenge.composables.BottomNavigationBar
import com.example.wtcchallenge.composables.Client
import com.example.wtcchallenge.composables.ClientRow
import com.example.wtcchallenge.composables.FiltroItem
import com.example.wtcchallenge.network.RetrofitInstance
import com.example.wtcchallenge.ui.theme.WTCChallengeTheme
import kotlinx.coroutines.launch

//INICIO DA FUNÇÃO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientListScreen(
    onMessagesClick: () -> Unit,
    onCampaignClick: () -> Unit,
    onClientClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    // ESTADO: Barra de busca
    var textoBusca by remember { mutableStateOf(TextFieldValue("")) }

    // ESTADO: Lista de clientes
    var clientes by remember { mutableStateOf<List<Client>>(emptyList()) }

    // ESTADO: Loading e Erro
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Coroutine Scope
    val scope = rememberCoroutineScope()

    // CARREGA OS CLIENTES DA API
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                isLoading = true
                errorMessage = null
                val lista = RetrofitInstance.api.getClients()
                clientes = lista
            } catch (e: Exception) {
                errorMessage = "Erro ao carregar clientes: ${e.message}"
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    // LÓGICA DE BUSCA E FILTRO
    val textoBuscaString = textoBusca.text
    val clientesFiltrados = remember(clientes, textoBuscaString) {
        if (textoBuscaString.isBlank()) {
            clientes
        } else {
            clientes.filter { cliente ->
                cliente.nome.contains(textoBuscaString, ignoreCase = true) ||
                        cliente.ramo.contains(textoBuscaString, ignoreCase = true)
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFF121417),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lista de Clientes", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121417),
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = {
                        // Recarregar lista
                        scope.launch {
                            try {
                                isLoading = true
                                errorMessage = null
                                val lista = RetrofitInstance.api.getClients()
                                clientes = lista
                            } catch (e: Exception) {
                                errorMessage = "Erro ao recarregar: ${e.message}"
                            } finally {
                                isLoading = false
                            }
                        }
                    }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Recarregar",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Adicionar Cliente",
                            tint = Color.White
                        )
                    }
                }
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // BARRA DE BUSCA
            TextField(
                value = textoBusca,
                onValueChange = { textoBusca = it },
                placeholder = { Text("Buscar cliente", color = Color(0xFF9EABBA)) },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF293038),
                    unfocusedContainerColor = Color(0xFF293038),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Ícone de busca",
                        tint = Color(0xFF9EABBA)
                    )
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // LINHA DE FILTROS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FiltroItem(text = "Tags")
                FiltroItem(text = "Score")
                FiltroItem(text = "Status")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // CONTEÚDO: Loading, Erro ou Lista
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
                    // LISTA DE CLIENTES
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(clientesFiltrados) { cliente ->
                            ClientRow(cliente = cliente)
                            HorizontalDivider(color = Color(0xFF293038), thickness = 1.dp)
                        }
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ClientListScreenPreview() {
    WTCChallengeTheme {
        Surface(color = Color(0xFF0D0D0D)) {
            ClientListScreen(onMessagesClick = {},
                onClientClick = {},
                onCampaignClick = {},
                onProfileClick = {}
            )
        }
    }
}
