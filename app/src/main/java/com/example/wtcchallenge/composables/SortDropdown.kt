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

// 🔸 Menu suspenso para ordenar lista (por nome ou score)
@Composable
fun SortDropdown(currentSort: String, onSortSelected: (String) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }
    val sortOptions = listOf("Nome (A-Z)", "Score (Maior)", "Score (Menor)")

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
                Text(currentSort, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.White, modifier = Modifier.height(18.dp))
            }
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier.background(Color(0xFF293038))
        ) {
            sortOptions.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            option,
                            color = if (option == currentSort) Color(0xFFFF9800) else Color.White
                        )
                    },
                    onClick = {
                        onSortSelected(option)
                        isExpanded = false
                    }
                )
            }
        }
    }
}


