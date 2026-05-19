package com.example.wtcchallenge.composables.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wtcchallenge.composables.*
import com.example.wtcchallenge.network.RetrofitInstance
import com.example.wtcchallenge.network.SessionManager
import com.example.wtcchallenge.network.dto.CampaignRequestDto
import com.example.wtcchallenge.ui.theme.WTCChallengeTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampaignScreen(
    onMessagesClick: () -> Unit,
    onCampaignClick: () -> Unit,
    onClientClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var titulo by remember { mutableStateOf(TextFieldValue("")) }
    var mensagem by remember { mutableStateOf(TextFieldValue("")) }
    var targetAudience by remember { mutableStateOf("Simple") }
    var isSending by remember { mutableStateOf(false) }
    var feedbackMsg by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val darkBackground = Color(0xFF121417)
    val buttonColor = Color(0xFF1E88E5)

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
        ) {
            SimpleTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = "Título",
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

            ImageUploadBox()
            Spacer(modifier = Modifier.height(16.dp))

            Text("Target Audience", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            TargetAudienceDropdown(
                currentAudience = targetAudience,
                onAudienceSelected = { targetAudience = it }
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
                        feedbackMsg = "Preencha título e mensagem."
                        return@Button
                    }
                    scope.launch {
                        isSending = true
                        feedbackMsg = null
                        try {
                            val operatorId = SessionManager.operatorId ?: run {
                                feedbackMsg = "Erro: sessão expirada."
                                return@launch
                            }
                            val campaign = RetrofitInstance.api.createCampaign(
                                CampaignRequestDto(
                                    titulo = titulo.text.trim(),
                                    mensagem = mensagem.text.trim(),
                                    targetAudience = targetAudience,
                                    operatorId = operatorId
                                )
                            )
                            RetrofitInstance.api.sendCampaign(campaign.id)
                            feedbackMsg = "Campanha enviada com sucesso!"
                            titulo = TextFieldValue("")
                            mensagem = TextFieldValue("")
                        } catch (e: Exception) {
                            feedbackMsg = "Erro ao enviar campanha: ${e.message}"
                        } finally {
                            isSending = false
                        }
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CampaignScreenPreview() {
    WTCChallengeTheme {
        Surface(color = Color(0xFF0D0D0D)) {
            CampaignScreen(
                onMessagesClick = {},
                onClientClick = {},
                onCampaignClick = {},
                onProfileClick = {}
            )
        }
    }
}
