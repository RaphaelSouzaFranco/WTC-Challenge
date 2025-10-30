package com.example.wtcchallenge.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    darkMode: Boolean,
    textColor: Color,
    secondaryColor: Color,
    onToggleDarkMode: () -> Unit
) {
    var notes by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121417))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Foto de perfil
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
        Text("Isabella Rossi", color = textColor, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tags


        Spacer(modifier = Modifier.height(20.dp))
        Text("Histórico de Mensagens", color = textColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))
        MessageItem("Mensagem enviada: Promoção de verão", "20 de Julho, 2:30 PM", textColor)
        MessageItem("Mensagem recebida: Dúvidas sobre o produto", "15 de Julho, 10:00 AM", textColor)
        MessageItem("Mensagem enviada: Bem-vindos!", "10 de Julho, 4:45 PM", textColor)

        Spacer(modifier = Modifier.height(16.dp))
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
                onValueChange = { notes = it },
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
