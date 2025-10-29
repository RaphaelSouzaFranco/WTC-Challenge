package com.example.wtcchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wtcchallenge.composables.LoginScreen
import com.example.wtcchallenge.composables.MessagesScreen
import com.example.wtcchallenge.composables.Screen
import com.example.wtcchallenge.ui.theme.WTCChallengeTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WTCChallengeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {innerPadding ->
                    WTCApp(modifier = Modifier.padding(innerPadding))
                }
                }
            }
        }
    }


@Composable
fun WTCApp(modifier: Modifier = Modifier) {
    val nav = rememberNavController()

    NavHost(navController= nav, startDestination = Screen.Login.route,modifier=modifier) {
        composable(Screen.Login.route){
            LoginScreen(
                onLogin = {nav.navigate(Screen.Messages.route)}
            )
        }
        composable(Screen.Messages.route){
            MessagesScreen(navController = nav)
        }
    }

    }

