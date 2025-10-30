package com.example.wtcchallenge.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
fun BottomNavigationBar(
    onMessagesClick: () -> Unit,
    onCampaignClick: () -> Unit,
    onClientClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var selectedItem by remember { mutableStateOf(0) }

    NavigationBar(
        containerColor = Color(0xFF1C1F24)
    ) {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = { selectedItem = 0; onMessagesClick() },
            icon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Conversas",
                    tint = Color.White
                )
            },
            label = { Text("Conversas", color = Color.White) }
        )

        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = { selectedItem = 1; onCampaignClick() },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Campanhas",
                    tint = Color.White
                )
            },
            label = { Text("Campanhas", color = Color.White) }
        )

        NavigationBarItem(
            selected = selectedItem == 2,
            onClick = { selectedItem = 2; onClientClick() },
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Clientes",
                    tint = Color.White
                )
            },
            label = { Text("Clientes", color = Color.White) }
        )

        NavigationBarItem(
            selected = selectedItem == 3,
            onClick = { selectedItem = 3; onProfileClick() },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    tint = Color.White
                )
            },
            label = { Text("Perfil", color = Color.White) }
        )
    }
}
