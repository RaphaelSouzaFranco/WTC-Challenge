package com.example.wtcchallenge.Screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.sp
import com.example.wtcchallenge.ui.theme.WTCChallengeTheme
import com.example.wtcchallenge.composables.BottomNavigationBar
import com.example.wtcchallenge.composables.ClientRow
import com.example.wtcchallenge.composables.FiltroItem
import com.example.wtcchallenge.model.Client
import com.example.wtcchallenge.network.RetrofitInstance
import com.example.wtcchallenge.network.SessionManager
import com.example.wtcchallenge.network.dto.ClientRequestDto
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientListScreen(
    onMessagesClick: () -> Unit,
    onCampaignClick: () -> Unit,
    onClientClick: () -> Unit,
    onProfileClick: () -> Unit,
    onTimelineClick: ((String) -> Unit)? = null,
    onInboxClick: ((String) -> Unit)? = null
) {
    var textoBusca by remember { mutableStateOf(TextFieldValue("")) }
    var clientes by remember { mutableStateOf<List<Client>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    fun loadClients(search: String? = null) {
        scope.launch {
            isLoading = true
            errorMessage = null
            try {
                clientes = RetrofitInstance.api.getClients(search = search?.ifBlank { null })
            } catch (e: Exception) {
                errorMessage = "Erro ao carregar clientes: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) { loadClients() }

    val clientesFiltrados = remember(clientes, textoBusca.text) {
        val q = textoBusca.text
        if (q.isBlank()) clientes
        else clientes.filter { c ->
            c.nome.contains(q, ignoreCase = true) || c.ramo.contains(q, ignoreCase = true)
        }
    }

    if (showAddDialog) {
        AddClientDialog(
            onDismiss = { showAddDialog = false },
            onSave = { request ->
                scope.launch {
                    try {
                        RetrofitInstance.api.createClient(request)
                        showAddDialog = false
                        loadClients(textoBusca.text)
                    } catch (e: Exception) {
                        errorMessage = "Erro ao criar cliente: ${e.message}"
                    }
                }
            }
        )
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
                    IconButton(onClick = { loadClients(textoBusca.text) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Recarregar", tint = Color.White)
                    }
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Adicionar Cliente", tint = Color.White)
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
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF9EABBA))
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                FiltroItem(text = "Tags")
                FiltroItem(text = "Score")
                FiltroItem(text = "Status")
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
                        Button(onClick = { loadClients() }) { Text("Tentar Novamente") }
                    }
                }
                else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(clientesFiltrados) { cliente ->
                        ClientRow(
                            cliente = cliente,
                            onTimelineClick = onTimelineClick,
                            onInboxClick = onInboxClick
                        )
                        HorizontalDivider(color = Color(0xFF293038), thickness = 1.dp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClientListScreenPreview() {
    WTCChallengeTheme {
        ClientListScreen(
            onMessagesClick = {},
            onCampaignClick = {},
            onClientClick = {},
            onProfileClick = {},
            onTimelineClick = {},
            onInboxClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClientDialog(
    onDismiss: () -> Unit,
    onSave: (ClientRequestDto) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var ramo by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Lead") }
    var tagsText by remember { mutableStateOf("") }
    var scoreText by remember { mutableStateOf("0") }
    var localError by remember { mutableStateOf<String?>(null) }

    val statusOptions = listOf("Lead", "Prospect", "Ativo", "Inativo")
    var statusExpanded by remember { mutableStateOf(false) }

    val fieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color(0xFF293038),
        unfocusedContainerColor = Color(0xFF293038),
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        cursorColor = Color.White,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1C1F24),
        title = { Text("Novo Cliente", color = Color.White, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(
                    value = nome,
                    onValueChange = { nome = it },
                    placeholder = { Text("Nome *", color = Color(0xFF9EABBA)) },
                    singleLine = true,
                    colors = fieldColors,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = numero,
                    onValueChange = { numero = it },
                    placeholder = { Text("Número *", color = Color(0xFF9EABBA)) },
                    singleLine = true,
                    colors = fieldColors,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = ramo,
                    onValueChange = { ramo = it },
                    placeholder = { Text("Ramo", color = Color(0xFF9EABBA)) },
                    singleLine = true,
                    colors = fieldColors,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenuBox(
                    expanded = statusExpanded,
                    onExpandedChange = { statusExpanded = !statusExpanded }
                ) {
                    TextField(
                        value = status,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                        colors = fieldColors,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = statusExpanded,
                        onDismissRequest = { statusExpanded = false },
                        modifier = Modifier.background(Color(0xFF293038))
                    ) {
                        statusOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, color = Color.White) },
                                onClick = {
                                    status = option
                                    statusExpanded = false
                                }
                            )
                        }
                    }
                }
                TextField(
                    value = tagsText,
                    onValueChange = { tagsText = it },
                    placeholder = { Text("Tags (separadas por vírgula)", color = Color(0xFF9EABBA)) },
                    singleLine = true,
                    colors = fieldColors,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = scoreText,
                    onValueChange = { scoreText = it.filter { ch -> ch.isDigit() } },
                    placeholder = { Text("Score (0-100)", color = Color(0xFF9EABBA)) },
                    singleLine = true,
                    colors = fieldColors,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                if (localError != null) {
                    Text(localError!!, color = Color.Red, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                when {
                    nome.isBlank() || numero.isBlank() ->
                        localError = "Nome e número são obrigatórios."
                    SessionManager.operatorId == null ->
                        localError = "Sessão expirada. Faça login novamente."
                    else -> {
                        localError = null
                        onSave(
                            ClientRequestDto(
                                nome = nome.trim(),
                                numero = numero.trim(),
                                ramo = ramo.trim(),
                                status = status,
                                tags = tagsText.split(",")
                                    .map { it.trim() }
                                    .filter { it.isNotBlank() },
                                score = scoreText.toIntOrNull()?.coerceIn(0, 100) ?: 0,
                                operatorId = SessionManager.operatorId!!
                            )
                        )
                    }
                }
            }) {
                Text("Salvar", color = Color(0xFF1E88E5), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color(0xFF9EABBA))
            }
        }
    )
}
