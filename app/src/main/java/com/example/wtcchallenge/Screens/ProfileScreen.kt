package com.example.wtcchallenge.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.wtcchallenge.ui.theme.WTCChallengeTheme
import com.example.wtcchallenge.composables.BottomNavigationBar
import com.example.wtcchallenge.composables.ProfileContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onMessagesClick: () -> Unit,
                  onCampaignClick: () -> Unit,
                  onClientClick: () -> Unit,
                  onProfileClick: () -> Unit) {
    var darkMode by remember { mutableStateOf(true) }
    val backgroundColor = if (darkMode) Color(0xFF121212) else Color(0xFFFFFFFF)
    val textColor = if (darkMode) Color.White else Color.Black
    val secondaryColor = if (darkMode) Color(0xFF1E1E1E) else Color(0xFFF3F3F3)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil do Operador", color = textColor) },
                navigationIcon = {
                    IconButton(onClick = {onClientClick()}) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = textColor
                        )
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
        ProfileContent(
            modifier = Modifier.padding(paddingValues),
            darkMode = darkMode,
            textColor = textColor,
            secondaryColor = secondaryColor,
            onToggleDarkMode = { darkMode = !darkMode }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    WTCChallengeTheme {
        ProfileScreen(onMessagesClick = {},
            onClientClick = {},
            onCampaignClick = {},
            onProfileClick = {})
    }
}
