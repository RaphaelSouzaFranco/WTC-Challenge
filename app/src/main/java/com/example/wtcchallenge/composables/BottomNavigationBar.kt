package com.example.wtcchallenge.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String) {
    NavigationBar(
        containerColor = Color(0xFF1C1F24)
    ) {
        NavigationBarItem(
            selected = currentRoute == Screen.Messages.route,
            onClick = { navController.navigate(Screen.Messages.route) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person, // Substituir por ícone de "chat"
                    contentDescription = "Conversas",
                    tint = Color.White
                )
            },
            label = { Text("Conversas", color = Color.White) }
        )

        NavigationBarItem(
            selected = currentRoute == Screen.Campaign.route,
            onClick = { navController.navigate(Screen.Campaign.route) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person, // Placeholder
                    contentDescription = "Campanhas",
                    tint = Color.White
                )
            },
            label = { Text("Campanhas", color = Color.White) }
        )

        NavigationBarItem(
            selected = currentRoute == Screen.Profile.route,
            onClick = { navController.navigate(Screen.Profile.route) },
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