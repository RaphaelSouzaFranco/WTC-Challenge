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
import androidx.navigation.compose.rememberNavController
import com.example.wtcchallenge.composables.* // Importa os novos componentes!
import com.example.wtcchallenge.ui.theme.WTCChallengeTheme

// =========================================================
// 3️⃣ TELA CAMPANHAS EXPRESSAS - LIMPA
// =========================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampaignScreen() {
    // 🔹 Estados
    var titulo by remember { mutableStateOf(TextFieldValue("")) }
    var mensagem by remember { mutableStateOf(TextFieldValue("")) }
    var targetAudience by remember { mutableStateOf("Simple") }

    // 🔹 Cores e Navegação
    val navController = rememberNavController()
    val currentRoute = Screen.Campaign.route
    val darkBackground = Color(0xFF121417)
    val buttonColor = Color(0xFF1E88E5)

    Scaffold(
        containerColor = darkBackground,
        topBar = {
            // 🔸 Barra superior
            CenterAlignedTopAppBar(
                title = { Text("Campanhas Expressas", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = darkBackground,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { /* Ação de Voltar */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                }
            )
        },
        bottomBar = {
            // 🔸 Barra inferior
            BottomNavigationBar(
                onMessagesClick = { navController.navigate(Screen.Messages.route) },
                onCampaignClick = { navController.navigate(Screen.Campaign.route) },
                onClientClick = { navController.navigate(Screen.Client.route) },
                onProfileClick = { navController.navigate(Screen.Profile.route) }
            )
        }
    ) { paddingValues ->
        // =========================================================
        //  CONTEÚDO PRINCIPAL (Chamada aos Componentes)
        // =========================================================
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // 1. Título
            SimpleTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = "Título",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 2. Mensagem
            SimpleTextField(
                value = mensagem,
                onValueChange = { mensagem = it },
                label = "Mensagem",
                minLines = 4,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 3. Upload de Imagem
            ImageUploadBox()
            Spacer(modifier = Modifier.height(16.dp))

            // 4. Público-Alvo (Target Audience)
            Text("Target Audience", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            TargetAudienceDropdown(
                currentAudience = targetAudience,
                onAudienceSelected = { targetAudience = it }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 5. Preview da Campanha
            Text("Preview", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            CampaignPreviewCard()
            Spacer(modifier = Modifier.height(32.dp))

            // 6. Botão Enviar
            Button(
                onClick = { /* Ação de Enviar */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Send Now", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}


// =========================================================
// 4️⃣ PREVIEW – VISUALIZAÇÃO NO ANDROID STUDIO
// =========================================================
@Preview(showBackground = true)
@Composable
fun CampaignScreenPreview() {
    WTCChallengeTheme {
        Surface(color = Color(0xFF0D0D0D)) {
            CampaignScreen()
        }
    }
}