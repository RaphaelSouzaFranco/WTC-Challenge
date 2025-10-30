package com.example.wtcchallenge.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wtcchallenge.ui.theme.WTCChallengeTheme

// =========================================================
// 1. DATA CLASS E CLASSES DE NAVEGAÇÃO
// =========================================================

data class Cliente(
    val nome: String,
    val numero: String,
    val ramo: String
)


// =========================================================
// 2. TELA PRINCIPAL
// =========================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientListScreen() {
    // ESTADO 1: Para a barra de busca
    var textoBusca by remember { mutableStateOf(TextFieldValue("")) }

    // ESTADO 2: Variáveis de navegação (CORREÇÃO DE ESCOPO)
    val navController: NavController = rememberNavController() // Para o Preview
    val currentRoute: String = Screen.Client.route

    // ESTADO 3: Lista de Clientes (Mutável para futuras adições/exclusões)
    val clientes = remember {
        mutableStateListOf(
            Cliente(nome = "ACME Corp", numero = "123456", ramo = "Tecnologia"),
            Cliente(nome = "Consultoria Alfa", numero = "789012", ramo = "Finanças"),
            Cliente(nome = "Logística Rápida", numero = "345678", ramo = "Logística"),
            Cliente(nome = "Estúdio Criativo", numero = "987654", ramo = "Marketing"),
            Cliente(nome = "Saúde Vital", numero = "109876", ramo = "Saúde"),
        )
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
        containerColor = (Color(0xFF121417)), // Fundo escuro
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lista de Clientes", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121417),
                    titleContentColor = Color.White
                ),
                actions = {
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
                onMessagesClick = { navController.navigate(Screen.Messages.route) },
                onCampaignClick = { navController.navigate(Screen.Campaign.route) },
                onClientClick = { navController.navigate(Screen.Client.route) },
                onProfileClick = { navController.navigate(Screen.Profile.route) }
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

            // LISTA DE CLIENTES
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(clientesFiltrados) { cliente -> // Usa a lista FILTRADA
                    ClientRow(cliente = cliente)
                    // Linha Divisória
                    Divider(color = Color(0xFF293038), thickness = 1.dp)
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
            ClientListScreen()
        }
    }
}