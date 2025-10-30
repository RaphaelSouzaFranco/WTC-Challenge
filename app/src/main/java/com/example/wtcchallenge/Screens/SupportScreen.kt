package com.example.wtcchallenge.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wtcchallenge.ui.theme.WTCChallengeTheme

data class ChatMessage(
    val text: String,
    val isFromUser: Boolean,
    val isButton: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(onMessagesClick: () -> Unit,
                  onCampaignClick: () -> Unit,
                  onClientClick: () -> Unit,
                  onProfileClick: () -> Unit,
                 ) {
    var messageText by remember { mutableStateOf(TextFieldValue("")) }
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage("Bom dia! Preciso de ajuda com meu pedido #12345.", true),
                ChatMessage("Bom dia, Ricardo! Verificando seu pedido agora. Aguarde um momento.", false),
                ChatMessage("Ok. Obrigado!", true),
                ChatMessage("Seu pedido está em trânsito e deve chegar em 2 dias úteis. Você pode rastreá-lo aqui:", false),
                ChatMessage("Acompanhar pedido", false, isButton = true),
                ChatMessage("Precisa de mais alguma coisa?", false)
            )
        )
    }

    Scaffold(
        containerColor = Color(0xFF121417),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Suporte",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Customer Support",
                            color = Color(0xFF9EABBA),
                            fontSize = 12.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {onMessagesClick()}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1C1F24)
                )
            )
        },
        bottomBar = {
            _root_ide_package_.com.example.wtcchallenge.composables.BottomNavigationBar(
                onMessagesClick = onMessagesClick,
                onCampaignClick = onCampaignClick,
                onClientClick = onClientClick,
                onProfileClick = onProfileClick
            )
        }
    )
    {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(messages) { message ->
                    when {
                        message.isButton -> _root_ide_package_.com.example.wtcchallenge.composables.TrackOrderButton()
                        message.isFromUser -> _root_ide_package_.com.example.wtcchallenge.composables.UserMessage(
                            message.text
                        )
                        else -> _root_ide_package_.com.example.wtcchallenge.composables.SupportMessage(
                            message.text
                        )
                    }
                }
            }

            _root_ide_package_.com.example.wtcchallenge.composables.ChatBottomBar(
                messageText = messageText,
                onMessageChange = { messageText = it },
                onSendClick = {
                    if (messageText.text.isNotBlank()) {
                        messages = messages + ChatMessage(messageText.text, true)
                        messageText = TextFieldValue("")
                    }
                }
            )
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SupportScreenPreview() {
    WTCChallengeTheme {
        val navController = rememberNavController()
        Surface(color = Color(0xFF0D0D0D)) {
            SupportScreen(onMessagesClick = {},
                onClientClick = {},
                onCampaignClick = {},
                onProfileClick = {})
        }
    }
}
