package com.example.wtcchallenge.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wtcchallenge.model.Conversation

@Composable
fun ConversationItem(
    conversation: Conversation,
    onClick: () -> Unit
) {
    val nomeExibido = conversation.client?.nome ?: "Conversa"
    val subtitulo = conversation.lastMessage ?: conversation.client?.ramo ?: ""

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF293038))
                .border(1.dp, Color(0xFF2F3540), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar",
                tint = Color(0xFF9EABBA),
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = nomeExibido,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Text(
                text = subtitulo,
                color = Color(0xFF9EABBA),
                fontSize = 14.sp,
                maxLines = 1
            )
        }

        if (conversation.unreadCount > 0) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(Color(0xFF007BFF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = conversation.unreadCount.toString(),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
