package com.example.wtcchallenge.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wtcchallenge.ui.theme.WTCChallengeTheme
import com.example.wtcchallenge.composables.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, currentRoute: String) {
    var darkMode by remember { mutableStateOf(true) }
    val backgroundColor = if (darkMode) Color(0xFF121212) else Color(0xFFFFFFFF)
    val textColor = if (darkMode) Color.White else Color.Black
    val secondaryColor = if (darkMode) Color(0xFF1E1E1E) else Color(0xFFF3F3F3)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil do Cliente", color = textColor) },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: ação de voltar */ }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = textColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = secondaryColor)
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentRoute = currentRoute
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        ProfileContent(
            modifier = Modifier.padding(paddingValues),
            darkMode = darkMode,
            textColor = textColor,
            secondaryColor = secondaryColor,
            onToggleDarkMode = { darkMode = !darkMode }
        )
    }
}

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
        Text("Score: 850", color = textColor.copy(alpha = 0.8f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Status: Active", color = textColor.copy(alpha = 0.8f))
            Box(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00FF4C))
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tags
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TagItem("VIP", secondaryColor, textColor)
            TagItem("Marketing", secondaryColor, textColor)
            TagItem("Engaged", secondaryColor, textColor)
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("Message History", color = textColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))
        MessageItem("Sent message: Summer promotion", "July 20th, 2:30 PM", textColor)
        MessageItem("Received message: Product inquiry", "July 15th, 10:00 AM", textColor)
        MessageItem("Sent message: Welcome", "July 10th, 4:45 PM", textColor)

        Spacer(modifier = Modifier.height(16.dp))
        Text("Quick Notes", color = textColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)

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
            Text("Dark Mode", color = textColor)
            Switch(
                checked = darkMode,
                onCheckedChange = { onToggleDarkMode() },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White)
            )
        }
    }
}

@Composable
fun TagItem(text: String, background: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .background(background, RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, color = textColor, fontSize = 12.sp)
    }
}

@Composable
fun MessageItem(title: String, time: String, color: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(title, color = color, fontSize = 14.sp)
        Text(time, color = color.copy(alpha = 0.7f), fontSize = 12.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    WTCChallengeTheme {
        val navController = rememberNavController()
        ProfileScreen(navController = navController, currentRoute = "profile")
    }
}
