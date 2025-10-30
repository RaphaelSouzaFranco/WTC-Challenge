package com.example.wtcchallenge.composables.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wtcchallenge.composables.ClientRow
import com.example.wtcchallenge.composables.Cliente
import com.example.wtcchallenge.composables.FiltroItem
import com.example.wtcchallenge.composables.ScoreFilterSlider
import com.example.wtcchallenge.composables.StatusFilterDropdown
import com.example.wtcchallenge.composables.BottomNavigationBar
import com.example.wtcchallenge.composables.Screen
import com.example.wtcchallenge.ui.theme.WTCChallengeTheme

// =========================================================
// 2️⃣ TELA PRINCIPAL – LISTAGEM DE CLIENTES
// =========================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientListScreen() {

    // 🔹 Estado da barra de busca
    var textoBusca by remember { mutableStateOf(TextFieldValue("")) }

    // 🔹 Estados de filtro e ordenação
    var tipoOrdenacao by remember { mutableStateOf("Nome (A-Z)") }
    var filtroStatusSelecionado by remember { mutableStateOf("Todos") }
    var filtroScoreMinimo by remember { mutableStateOf(0) }

    // 🔹 Controle de navegação (mockado)
    val navController: NavController = rememberNavController()
    val currentRoute: String = Screen.Client.route

    // 🔹 Lista inicial de clientes (mock)
    val clientes = remember {
        mutableStateListOf(
            Cliente("ACME Corp", "123456", "Tecnologia", "Ativo", listOf("VIP", "Contrato"), 950),
            Cliente("Consultoria Alfa", "789012", "Finanças", "Ativo", listOf("VIP", "Contrato"), 950),
            Cliente("Logística Rápida", "345678", "Logística", "Novo", listOf("Lead"), 550),
            Cliente("Estúdio Criativo", "987654", "Marketing", "Ativo", listOf("Acompanhamento"), 750),
            Cliente("Saúde Vital", "109876", "Saúde", "Inativo", listOf("Antigo"), 300),
        )
    }

    // =========================================================
    //  FILTRO, BUSCA E ORDENAÇÃO DE CLIENTES
    // =========================================================
    val textoBuscaString = textoBusca.text

    val clientesFiltrados = remember(
        clientes,
        textoBuscaString,
        filtroStatusSelecionado,
        filtroScoreMinimo,
        tipoOrdenacao
    ) {
        // 1️⃣ Filtra pelo texto digitado (nome ou ramo)
        val clientesAposBusca = if (textoBuscaString.isBlank()) clientes else {
            clientes.filter {
                it.nome.contains(textoBuscaString, ignoreCase = true) ||
                        it.ramo.contains(textoBuscaString, ignoreCase = true)
            }
        }

        // 2️⃣ Filtra pelo status selecionado
        val clientesAposStatus = if (filtroStatusSelecionado == "Todos") {
            clientesAposBusca
        } else {
            clientesAposBusca.filter { it.status == filtroStatusSelecionado }
        }

        // 3️⃣ Filtra por score mínimo
        val clientesAposScore = clientesAposStatus.filter { it.score >= filtroScoreMinimo }

        // 4️⃣ Ordena conforme o tipo de ordenação escolhido
        when (tipoOrdenacao) {
            "Nome (A-Z)" -> clientesAposScore.sortedBy { it.nome }
            "Score (Maior)" -> clientesAposScore.sortedByDescending { it.score }
            "Score (Menor)" -> clientesAposScore.sortedBy { it.score }
            else -> clientesAposScore
        }
    }

    // =========================================================
    //  ESTRUTURA DE TELA (SCAFFOLD)
    // =========================================================
    Scaffold(
        containerColor = Color(0xFF121417),
        topBar = {
            // 🔸 Barra superior com título e botão de adicionar
            CenterAlignedTopAppBar(
                title = { Text("Lista de Clientes", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121417),
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = {
                        clientes.add(
                            Cliente(
                                "Novo Cliente", "00000000000000",
                                "Recente", "Novo", listOf("Lead"), 10
                            )
                        )
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Adicionar Cliente", tint = Color.White)
                    }
                }
            )
        },
        bottomBar = {
            // 🔸 Barra inferior de navegação
            BottomNavigationBar(navController = navController, currentRoute = currentRoute)
        }
    ) { paddingValues ->

        // =========================================================
        //  CONTEÚDO PRINCIPAL
        // =========================================================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 Campo de busca
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
                    Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color(0xFF9EABBA))
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Linha com filtros (Score, Status, etc.)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ScoreFilterSlider(
                    currentScore = filtroScoreMinimo,
                    onScoreChanged = { filtroScoreMinimo = it }
                )

                FiltroItem(text = "Score")

                StatusFilterDropdown(
                    currentStatus = filtroStatusSelecionado,
                    onStatusSelected = { filtroStatusSelecionado = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Lista de clientes
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(clientesFiltrados) { cliente ->
                    ClientRow(cliente = cliente)
                    Divider(color = Color(0xFF293038), thickness = 1.dp)
                }
            }
        }
    }
}


// =========================================================
// 4️⃣ PREVIEW – VISUALIZAÇÃO NO ANDROID STUDIO
// =========================================================
@Preview(showBackground = true)
@Composable
fun ClientListScreenPreview() {
    WTCChallengeTheme {
        Surface(color = Color(0xFF0D0D0D)) {
            ClientListScreen()
        }
    }
}
