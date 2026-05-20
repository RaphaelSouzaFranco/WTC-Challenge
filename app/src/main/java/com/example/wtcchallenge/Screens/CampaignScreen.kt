package com.example.wtcchallenge.composables.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wtcchallenge.ui.theme.WTCChallengeTheme
import com.example.wtcchallenge.composables.*
import com.example.wtcchallenge.model.Client
import com.example.wtcchallenge.model.Segment
import com.example.wtcchallenge.network.RetrofitInstance
import com.example.wtcchallenge.network.SessionManager
import com.example.wtcchallenge.network.dto.ABTestRequestDto
import com.example.wtcchallenge.network.dto.CampaignRequestDto
import com.example.wtcchallenge.network.dto.MessageRequestDto
import com.example.wtcchallenge.network.dto.ScheduleRequestDto
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.Instant
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampaignScreen(
    onMessagesClick: () -> Unit,
    onCampaignClick: () -> Unit,
    onClientClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSegmentsClick: (() -> Unit)? = null
) {
    var titulo by remember { mutableStateOf(TextFieldValue("")) }
    var mensagem by remember { mutableStateOf(TextFieldValue("")) }
    var targetAudience by remember { mutableStateOf("Simple") }
    var isSending by remember { mutableStateOf(false) }
    var feedbackMsg by remember { mutableStateOf<String?>(null) }

    var segments by remember { mutableStateOf<List<Segment>>(emptyList()) }
    var selectedSegmentId by remember { mutableStateOf<String?>(null) }

    var scheduleHours by remember { mutableStateOf(TextFieldValue("")) }
    var showABTestDialog by remember { mutableStateOf(false) }
    var showRecipientsDialog by remember { mutableStateOf(false) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val scope = rememberCoroutineScope()
    val darkBackground = Color(0xFF121417)
    val buttonColor = Color(0xFF1E88E5)

    LaunchedEffect(Unit) {
        try {
            val operatorId = SessionManager.operatorId ?: return@LaunchedEffect
            segments = RetrofitInstance.api.getSegments(operatorId)
        } catch (_: Exception) {}
    }

    if (showRecipientsDialog) {
        RecipientsDialog(
            onDismiss = { showRecipientsDialog = false },
            onConfirm = { selectedClients ->
                showRecipientsDialog = false
                scope.launch {
                    isSending = true
                    feedbackMsg = null
                    try {
                        val operatorId = SessionManager.operatorId ?: run {
                            feedbackMsg = "Erro: sessao expirada."
                            return@launch
                        }
                        val campaign = RetrofitInstance.api.createCampaign(
                            CampaignRequestDto(
                                titulo = titulo.text.trim(),
                                mensagem = mensagem.text.trim(),
                                targetAudience = targetAudience,
                                segmentId = selectedSegmentId,
                                operatorId = operatorId
                            )
                        )
                        imageUri?.let { uri ->
                            withContext(Dispatchers.IO) {
                                val inputStream = context.contentResolver.openInputStream(uri)
                                val bytes = inputStream?.readBytes() ?: byteArrayOf()
                                inputStream?.close()
                                val contentType = context.contentResolver.getType(uri) ?: "image/jpeg"
                                val requestBody = bytes.toRequestBody(contentType.toMediaTypeOrNull())
                                val part = MultipartBody.Part.createFormData(
                                    "file", "campaign_image.jpg", requestBody
                                )
                                RetrofitInstance.api.uploadCampaignMedia(campaign.id, part)
                            }
                        }

                        val conteudo = "📢 ${titulo.text.trim()}\n\n${mensagem.text.trim()}"
                        var entreguesComSucesso = 0
                        selectedClients.forEach { cliente ->
                            try {
                                val conv = RetrofitInstance.api.getOrCreateConversation(
                                    mapOf("clientId" to cliente.id, "operatorId" to operatorId)
                                )
                                RetrofitInstance.api.sendMessage(
                                    conv.id,
                                    MessageRequestDto(
                                        senderId = operatorId,
                                        senderType = "OPERATOR",
                                        content = conteudo
                                    )
                                )
                                entreguesComSucesso++
                            } catch (_: Exception) { /* tenta os próximos */ }
                        }

                        RetrofitInstance.api.sendCampaign(campaign.id)

                        feedbackMsg = "Campanha entregue para $entreguesComSucesso de ${selectedClients.size} cliente(s)."
                        titulo = TextFieldValue("")
                        mensagem = TextFieldValue("")
                        scheduleHours = TextFieldValue("")
                        imageUri = null
                    } catch (e: Exception) {
                        feedbackMsg = "Erro ao enviar campanha: ${e.message}"
                    } finally {
                        isSending = false
                    }
                }
            }
        )
    }

    if (showABTestDialog) {
        ABTestDialog(
            onDismiss = { showABTestDialog = false },
            onConfirm = { tituloB, mensagemB ->
                showABTestDialog = false
                scope.launch {
                    isSending = true
                    feedbackMsg = null
                    try {
                        val operatorId = SessionManager.operatorId ?: return@launch
                        val campaign = RetrofitInstance.api.createCampaign(
                            CampaignRequestDto(
                                titulo = titulo.text.trim(),
                                mensagem = mensagem.text.trim(),
                                targetAudience = targetAudience,
                                segmentId = selectedSegmentId,
                                operatorId = operatorId
                            )
                        )
                        RetrofitInstance.api.createABTest(
                            campaign.id,
                            ABTestRequestDto(tituloB = tituloB, mensagemB = mensagemB)
                        )
                        feedbackMsg = "Teste A/B criado com sucesso!"
                        titulo = TextFieldValue("")
                        mensagem = TextFieldValue("")
                    } catch (e: Exception) {
                        feedbackMsg = "Erro ao criar teste A/B: ${e.message}"
                    } finally {
                        isSending = false
                    }
                }
            }
        )
    }

    Scaffold(
        containerColor = darkBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Campanhas Expressas", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = darkBackground,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { onMessagesClick() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
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
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SimpleTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = "Titulo",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            SimpleTextField(
                value = mensagem,
                onValueChange = { mensagem = it },
                label = "Mensagem",
                minLines = 4,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            ImageUploadBox(
                imageUri = imageUri,
                onPickImage = { imagePickerLauncher.launch("image/*") },
                onRemoveImage = { imageUri = null }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Target Audience", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            TargetAudienceDropdown(
                currentAudience = targetAudience,
                onAudienceSelected = { targetAudience = it }
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (targetAudience == "Segment" && segments.isNotEmpty()) {
                Text("Segmento", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                SegmentDropdown(
                    segments = segments,
                    selectedId = selectedSegmentId,
                    onSelected = { selectedSegmentId = it }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (onSegmentsClick != null) {
                TextButton(onClick = onSegmentsClick) {
                    Text("Gerenciar Segmentos", color = Color(0xFF1E88E5), fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text("Agendamento (opcional)", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            SimpleTextField(
                value = scheduleHours,
                onValueChange = { scheduleHours = it },
                label = "Horas a partir de agora (ex: 24)",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Preview", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            CampaignPreviewCard()
            Spacer(modifier = Modifier.height(16.dp))

            if (feedbackMsg != null) {
                Text(
                    text = feedbackMsg!!,
                    color = if (feedbackMsg!!.startsWith("Erro")) Color.Red else Color(0xFF4CAF50),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (titulo.text.isBlank() || mensagem.text.isBlank()) {
                        feedbackMsg = "Preencha titulo e mensagem."
                        return@Button
                    }
                    val hours = scheduleHours.text.trim().toLongOrNull()
                    if (hours != null && hours > 0) {
                        // Fluxo de agendamento (sem entrega imediata)
                        scope.launch {
                            isSending = true
                            feedbackMsg = null
                            try {
                                val operatorId = SessionManager.operatorId ?: run {
                                    feedbackMsg = "Erro: sessao expirada."
                                    return@launch
                                }
                                val campaign = RetrofitInstance.api.createCampaign(
                                    CampaignRequestDto(
                                        titulo = titulo.text.trim(),
                                        mensagem = mensagem.text.trim(),
                                        targetAudience = targetAudience,
                                        segmentId = selectedSegmentId,
                                        operatorId = operatorId
                                    )
                                )
                                imageUri?.let { uri ->
                                    withContext(Dispatchers.IO) {
                                        val inputStream = context.contentResolver.openInputStream(uri)
                                        val bytes = inputStream?.readBytes() ?: byteArrayOf()
                                        inputStream?.close()
                                        val contentType = context.contentResolver.getType(uri) ?: "image/jpeg"
                                        val requestBody = bytes.toRequestBody(contentType.toMediaTypeOrNull())
                                        val part = MultipartBody.Part.createFormData(
                                            "file", "campaign_image.jpg", requestBody
                                        )
                                        RetrofitInstance.api.uploadCampaignMedia(campaign.id, part)
                                    }
                                }
                                val scheduledAt = Instant.now().plus(hours, ChronoUnit.HOURS).toString()
                                RetrofitInstance.api.scheduleCampaign(
                                    campaign.id,
                                    ScheduleRequestDto(scheduledAt = scheduledAt)
                                )
                                feedbackMsg = "Campanha agendada para ${hours}h a partir de agora!"
                                titulo = TextFieldValue("")
                                mensagem = TextFieldValue("")
                                scheduleHours = TextFieldValue("")
                                imageUri = null
                            } catch (e: Exception) {
                                feedbackMsg = "Erro ao agendar campanha: ${e.message}"
                            } finally {
                                isSending = false
                            }
                        }
                    } else {
                        // Envio imediato: abre seleção de destinatários
                        feedbackMsg = null
                        showRecipientsDialog = true
                    }
                },
                enabled = !isSending,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isSending) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
                } else {
                    Text("Send Now", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    if (titulo.text.isBlank() || mensagem.text.isBlank()) {
                        feedbackMsg = "Preencha titulo e mensagem da variante A primeiro."
                        return@OutlinedButton
                    }
                    showABTestDialog = true
                },
                enabled = !isSending,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFFA726))
            ) {
                Text("Criar Teste A/B", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ABTestDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var tituloB by remember { mutableStateOf(TextFieldValue("")) }
    var mensagemB by remember { mutableStateOf(TextFieldValue("")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E2328),
        title = { Text("Variante B do Teste A/B", color = Color.White) },
        text = {
            Column {
                Text("O formulario atual sera a Variante A.", color = Color(0xFF9EABBA), fontSize = 13.sp)
                Spacer(modifier = Modifier.height(12.dp))
                TextField(
                    value = tituloB,
                    onValueChange = { tituloB = it },
                    placeholder = { Text("Titulo da variante B", color = Color(0xFF9EABBA)) },
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
                    value = mensagemB,
                    onValueChange = { mensagemB = it },
                    placeholder = { Text("Mensagem da variante B", color = Color(0xFF9EABBA)) },
                    minLines = 3,
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
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (tituloB.text.isNotBlank() && mensagemB.text.isNotBlank()) {
                        onConfirm(tituloB.text.trim(), mensagemB.text.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726))
            ) { Text("Criar Teste", color = Color.White) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = Color(0xFF9EABBA)) }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CampaignScreenPreview() {
    WTCChallengeTheme {
        CampaignScreen(
            onMessagesClick = {},
            onCampaignClick = {},
            onClientClick = {},
            onProfileClick = {},
            onSegmentsClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipientsDialog(
    onDismiss: () -> Unit,
    onConfirm: (List<Client>) -> Unit
) {
    var clients by remember { mutableStateOf<List<Client>>(emptyList()) }
    var selectedIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            clients = RetrofitInstance.api.getClients()
        } catch (e: Exception) {
            errorMessage = "Erro ao carregar clientes: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1C1F24),
        title = {
            Text(
                "Selecionar destinatários",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Box(modifier = Modifier.heightIn(min = 200.dp, max = 400.dp)) {
                when {
                    isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                    errorMessage != null -> Text(errorMessage!!, color = Color.Red)
                    clients.isEmpty() -> Text(
                        "Nenhum cliente cadastrado. Cadastre clientes primeiro.",
                        color = Color(0xFF9EABBA),
                        fontSize = 14.sp
                    )
                    else -> Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "${selectedIds.size}/${clients.size} selecionados",
                                color = Color(0xFF9EABBA),
                                fontSize = 13.sp
                            )
                            TextButton(onClick = {
                                selectedIds = if (selectedIds.size == clients.size) {
                                    emptySet()
                                } else {
                                    clients.map { it.id }.toSet()
                                }
                            }) {
                                Text(
                                    if (selectedIds.size == clients.size) "Limpar" else "Selecionar todos",
                                    color = Color(0xFF1E88E5),
                                    fontSize = 13.sp
                                )
                            }
                        }
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(clients) { cliente ->
                                val checked = selectedIds.contains(cliente.id)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedIds = if (checked) {
                                                selectedIds - cliente.id
                                            } else {
                                                selectedIds + cliente.id
                                            }
                                        }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = checked,
                                        onCheckedChange = {
                                            selectedIds = if (it) {
                                                selectedIds + cliente.id
                                            } else {
                                                selectedIds - cliente.id
                                            }
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = Color(0xFF1E88E5),
                                            uncheckedColor = Color(0xFF9EABBA)
                                        )
                                    )
                                    Column(modifier = Modifier.padding(start = 4.dp)) {
                                        Text(
                                            cliente.nome,
                                            color = Color.White,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        if (cliente.ramo.isNotBlank()) {
                                            Text(
                                                cliente.ramo,
                                                color = Color(0xFF9EABBA),
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val selected = clients.filter { it.id in selectedIds }
                    if (selected.isNotEmpty()) onConfirm(selected)
                },
                enabled = selectedIds.isNotEmpty()
            ) {
                Text(
                    "Enviar (${selectedIds.size})",
                    color = if (selectedIds.isNotEmpty()) Color(0xFF1E88E5) else Color(0xFF666666),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color(0xFF9EABBA))
            }
        }
    )
}
