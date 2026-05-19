package com.example.wtcchallenge.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wtcchallenge.model.Operator

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    operator: Operator,
    darkMode: Boolean,
    textColor: Color,
    secondaryColor: Color,
    onToggleDarkMode: () -> Unit,
    onNotesChange: (String) -> Unit
) {
    var notes by remember(operator.notas) { mutableStateOf(operator.notas ?: "") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121417))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFF2E2E2E)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(operator.nome.ifBlank { "Operador" }, color = textColor, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(operator.cargo.ifBlank { "" }, color = Color(0xFF9EABBA), fontSize = 14.sp)
        Text(operator.email, color = Color(0xFF9EABBA), fontSize = 12.sp)

        Spacer(modifier = Modifier.height(20.dp))
        Text("Notas", color = textColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(secondaryColor, RoundedCornerShape(10.dp))
                .padding(8.dp)
        ) {
            BasicTextField(
                value = notes,
                onValueChange = {
                    notes = it
                    onNotesChange(it)
                },
                textStyle = TextStyle(color = textColor, fontSize = 14.sp),
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Modo Escuro", color = textColor)
            Switch(
                checked = darkMode,
                onCheckedChange = { onToggleDarkMode() },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White)
            )
        }
    }
}
