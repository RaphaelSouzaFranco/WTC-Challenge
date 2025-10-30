package com.example.wtcchallenge.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.wtcchallenge.composables.Client

// 🔸 Cada linha da lista de clientes
@Composable
fun ClientRow(cliente: Client) {
    var isExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Esquerda: Avatar + Nome/Ramo
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFF424242)),
                contentAlignment = Alignment.Center
            ) {}
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(cliente.nome, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(cliente.ramo, color = Color(0xFF9EABBA), fontSize = 12.sp)
            }
        }

        // Direita: Ícone de score (expansível) + status
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isExpanded) {
                Icon(Icons.Default.Star, null, tint = Color(0xFFFFCC00), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Score: ${cliente.score}", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.width(8.dp))
            }

            Box(
                modifier = Modifier.size(8.dp).background(Color(0xFF2E7D32), shape = CircleShape)
            )
        }
    }
}
