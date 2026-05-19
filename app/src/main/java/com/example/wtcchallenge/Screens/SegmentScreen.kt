package com.example.wtcchallenge.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.example.wtcchallenge.ui.theme.WTCChallengeTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wtcchallenge.model.Segment
import com.example.wtcchallenge.network.RetrofitInstance
import com.example.wtcchallenge.network.SessionManager
import com.example.wtcchallenge.network.dto.SegmentRequestDto
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegmentScreen(onBack: () -> Unit) {
    var segments by remember { mutableStateOf<List<Segment>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val darkBackground = Color(0xFF121417)

    fun loadSegments() {
        scope.launch {
            isLoading = true
            try {
                val operatorId = SessionManager.operatorId ?: return@launch
                segments = RetrofitInstance.api.getSegments(operatorId)
            } catch (e: Exception) {
                errorMessage = "Erro ao carregar segmentos: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) { loadSegments() }

    if (showCreateDialog) {
        CreateSegmentDialog(
            onDismiss = { showCreateDialog = false },
            onCreated = {
                showCreateDialog = false
                loadSegments()
            }
        )
    }

    Scaffold(
        containerColor = darkBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Segmentos", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = darkBackground,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { showCreateDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Novo Segmento", tint = Color.White)
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
                        Button(onClick = { loadSegments() }) { Text("Tentar Novamente") }
                    }
                }
                segments.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhum segmento criado", color = Color(0xFF9EABBA), fontSize = 16.sp)
                }
                else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(segments) { segment ->
                        SegmentCard(
                            segment = segment,
                            onDelete = {
                                scope.launch {
                                    try {
                                        RetrofitInstance.api.deleteSegment(segment.id)
                                        loadSegments()
                                    } catch (_: Exception) {}
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SegmentCard(segment: Segment, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF293038)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(segment.nome, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                if (!segment.descricao.isNullOrBlank()) {
                    Text(segment.descricao, color = Color(0xFF9EABBA), fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("${segment.clientCount} clientes", color = Color(0xFF1E88E5), fontSize = 13.sp)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = Color(0xFF9EABBA))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSegmentDialog(onDismiss: () -> Unit, onCreated: () -> Unit) {
    var nome by remember { mutableStateOf(TextFieldValue("")) }
    var descricao by remember { mutableStateOf(TextFieldValue("")) }
    var statusFilter by remember { mutableStateOf(TextFieldValue("")) }
    var tagFilter by remember { mutableStateOf(TextFieldValue("")) }
    var isSaving by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E2328),
        title = { Text("Novo Segmento", color = Color.White) },
        text = {
            Column {
                TextField(
                    value = nome,
                    onValueChange = { nome = it },
                    placeholder = { Text("Nome do segmento", color = Color(0xFF9EABBA)) },
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
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    placeholder = { Text("Descricao (opcional)", color = Color(0xFF9EABBA)) },
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
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Criterios de segmentacao", color = Color(0xFF9EABBA), fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = statusFilter,
                    onValueChange = { statusFilter = it },
                    placeholder = { Text("Status (ex: Ativo)", color = Color(0xFF9EABBA)) },
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
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = tagFilter,
                    onValueChange = { tagFilter = it },
                    placeholder = { Text("Tag (ex: VIP)", color = Color(0xFF9EABBA)) },
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
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                if (errorMsg != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMsg!!, color = Color.Red, fontSize = 13.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nome.text.isBlank()) {
                        errorMsg = "Nome e obrigatorio"
                        return@Button
                    }
                    scope.launch {
                        isSaving = true
                        try {
                            val operatorId = SessionManager.operatorId ?: return@launch
                            val criterios = mutableMapOf<String, Any>()
                            if (statusFilter.text.isNotBlank()) criterios["status"] = statusFilter.text.trim()
                            if (tagFilter.text.isNotBlank()) criterios["tag"] = tagFilter.text.trim()

                            RetrofitInstance.api.createSegment(
                                SegmentRequestDto(
                                    nome = nome.text.trim(),
                                    descricao = descricao.text.trim().ifBlank { null },
                                    operatorId = operatorId,
                                    criterios = criterios.ifEmpty { null }
                                )
                            )
                            onCreated()
                        } catch (e: Exception) {
                            errorMsg = "Erro: ${e.message}"
                        } finally {
                            isSaving = false
                        }
                    }
                },
                enabled = !isSaving,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
            ) {
                if (isSaving) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp))
                else Text("Criar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color(0xFF9EABBA))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SegmentScreenPreview() {
    WTCChallengeTheme {
        Surface(color = Color(0xFF0D0D0D)) {
            SegmentScreen(onBack = {})
        }
    }
}
