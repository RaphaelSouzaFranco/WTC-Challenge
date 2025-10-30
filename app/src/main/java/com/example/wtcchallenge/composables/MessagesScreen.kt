package com.example.wtcchallenge.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wtcchallenge.ui.theme.WTCChallengeTheme

@Composable
fun MessagesScreen(navController: NavController) {
    var selectedFilter by remember { mutableStateOf("Todos") }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    val clientes = listOf(
        "Cliente 1", "Cliente 2", "Cliente 3", "Cliente 4", "Cliente 5", "Cliente 6"
    )

    val filteredList = clientes.filter {
        it.contains(searchQuery.text, ignoreCase = true)
    }

    Scaffold(
        containerColor = Color(0xFF121417),
        bottomBar = {
            BottomNavigationBar(
                onMessagesClick = { navController.navigate(Screen.Messages.route) },
                onCampaignClick = { navController.navigate(Screen.Campaign.route) },
                onClientClick = { navController.navigate(Screen.Client.route) },
                onProfileClick = { navController.navigate(Screen.Profile.route) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Conversas",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { /* ação futura */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SelectableButton("Todos", selectedFilter) { selectedFilter = "Todos" }
                SelectableButton("Clientes", selectedFilter) { selectedFilter = "Clientes" }
                SelectableButton("Grupos", selectedFilter) { selectedFilter = "Grupos" }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredList) { nome ->
                    ConversationItem(nome)
                }
            }
        }
    }
}







@Preview(showBackground = true)
@Composable
fun MessagesScreenPreview() {
    WTCChallengeTheme {
        val navController = rememberNavController()
        MessagesScreen(navController = navController)
    }
}
