package com.example.wtcchallenge.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
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

// 🔸 Slider dentro de um diálogo para escolher score mínimo
@Composable
fun ScoreFilterSlider(currentScore: Int, onScoreChanged: (Int) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Surface(
        shape = MaterialTheme.shapes.medium,
        color = Color(0xFF293038),
        modifier = Modifier.clickable { showDialog = true }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text("Score: ≥ $currentScore", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.White, modifier = Modifier.height(18.dp))
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Score Mínimo", color = Color.White) },
            text = {
                Column {
                    Text("Filtrar Score Mínimo: $currentScore", color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Slider(
                        value = currentScore.toFloat(),
                        onValueChange = { onScoreChanged(it.toInt()) },
                        valueRange = 0f..100f,
                        steps = 99,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF4CAF50),
                            activeTrackColor = Color(0xFF4CAF50),
                            inactiveTrackColor = Color(0xFF3B444C)
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK", color = Color(0xFF4CAF50))
                }
            },
            containerColor = Color(0xFF1C1F24)
        )
    }
}
