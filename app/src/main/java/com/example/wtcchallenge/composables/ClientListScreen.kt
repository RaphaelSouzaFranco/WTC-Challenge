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
                        Icon(Icons.Default.Add, contentDescription = "Adicionar Cliente", tint = Color.White)
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, currentRoute = currentRoute)
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
                    Icon(Icons.Default.Search, contentDescription = "Ícone de busca", tint = Color(0xFF9EABBA))
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

// =========================================================
// 3. COMPONENTES REUTILIZÁVEIS
// =========================================================

@Composable
fun ClientRow(cliente: Cliente) {
    // Estado para expandir/recolher o número do cliente
    var isExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded } // Torna a linha clicável
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // LADO ESQUERDO: Foto + Nome/Ramo
        Row(verticalAlignment = Alignment.CenterVertically) {

            // FOTO CIRCULAR (Placeholder)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF424242)),
                contentAlignment = Alignment.Center
            ) {}

            Spacer(modifier = Modifier.width(16.dp))

            // NOME E RAMO
            Column {
                Text(
                    text = cliente.nome,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = cliente.ramo,
                    color = Color(0xFF9EABBA),
                    fontSize = 12.sp
                )
            }
        }

        // LADO DIREITO: Número (Se Expandido) + Status
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isExpanded) {
                Text(
                    text = cliente.numero,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            // Ícone de Status (Bolha Verde)
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color(0xFF2E7D32), shape = CircleShape) // Usamos CircleShape aqui também
            )
        }
    }
}

@Composable
fun FiltroItem(text: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF293038),
        modifier = Modifier.clickable {}
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(text = text, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Filtro de $text",
                tint = Color.White,
                modifier = Modifier.height(18.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ClientListScreenPreview() {
    WTCChallengeTheme {
        Surface(color = Color(0xFF0D0D0D)) {
            // Chama a tela principal, que agora tem a lógica de navegação dentro
            ClientListScreen()
        }
    }
}