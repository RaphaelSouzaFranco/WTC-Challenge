package com.example.wtcchallenge.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wtcchallenge.composables.BottomNavigationBar
import com.example.wtcchallenge.composables.ProfileContent
import com.example.wtcchallenge.model.Operator
import com.example.wtcchallenge.network.RetrofitInstance
import com.example.wtcchallenge.network.SessionManager
import com.example.wtcchallenge.ui.theme.WTCChallengeTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onMessagesClick: () -> Unit,
    onCampaignClick: () -> Unit,
    onClientClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogout: () -> Unit = {}
) {
    var operator by remember { mutableStateOf(Operator()) }
    var darkMode by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(true) }
    var pendingNotes by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val id = SessionManager.operatorId ?: return@LaunchedEffect
        try {
            operator = RetrofitInstance.api.getOperator(id)
            darkMode = operator.darkMode
        } catch (e: Exception) {
            // mantém Operator vazio
        } finally {
            isLoading = false
        }
    }

    fun saveProfile() {
        val id = SessionManager.operatorId ?: return
        scope.launch {
            try {
                val updated = RetrofitInstance.api.updateOperator(
                    id,
                    operator.copy(
                        notas = pendingNotes ?: operator.notas,
                        darkMode = darkMode
                    )
                )
                operator = updated
                darkMode = updated.darkMode
            } catch (e: Exception) {
                // falha silenciosa
            }
        }
    }

    val backgroundColor = if (darkMode) Color(0xFF121212) else Color(0xFFFFFFFF)
    val textColor = if (darkMode) Color.White else Color.Black
    val secondaryColor = if (darkMode) Color(0xFF1E1E1E) else Color(0xFFF3F3F3)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil do Operador", color = textColor) },
                navigationIcon = {
                    IconButton(onClick = { onClientClick() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = textColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = secondaryColor)
            )
        },
        bottomBar = {
            BottomNavigationBar(
                onMessagesClick = onMessagesClick,
                onCampaignClick = onCampaignClick,
                onClientClick = onClientClick,
                onProfileClick = onProfileClick
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        } else {
            Column(modifier = Modifier.padding(paddingValues)) {
                ProfileContent(
                    operator = operator,
                    darkMode = darkMode,
                    textColor = textColor,
                    secondaryColor = secondaryColor,
                    onToggleDarkMode = {
                        darkMode = !darkMode
                        saveProfile()
                    },
                    onNotesChange = { pendingNotes = it }
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        SessionManager.clear()
                        onLogout()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sair da Conta", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    WTCChallengeTheme {
        ProfileScreen(onMessagesClick = {}, onClientClick = {}, onCampaignClick = {}, onProfileClick = {})
    }
}
