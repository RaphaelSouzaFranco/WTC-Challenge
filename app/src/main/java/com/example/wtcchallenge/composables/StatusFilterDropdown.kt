package com.example.wtcchallenge.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

// 🔸 Dropdown de seleção de status (Ativo, Novo, etc.)
@Composable
fun StatusFilterDropdown(currentStatus: String, onStatusSelected: (String) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }
    val statusOptions = listOf("Todos", "Ativo", "Novo", "Inativo")

    Box {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color(0xFF293038),
            modifier = Modifier.clickable { isExpanded = true }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text("Status: $currentStatus", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.White, modifier = Modifier.height(18.dp))
            }
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier.background(Color(0xFF293038))
        ) {
            statusOptions.forEach { status ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = status,
                            color = if (status == currentStatus) Color(0xFF4CAF50) else Color.White
                        )
                    },
                    onClick = {
                        onStatusSelected(status)
                        isExpanded = false
                    }
                )
            }
        }
    }
}
