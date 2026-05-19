package com.example.wtcchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wtcchallenge.Screens.ClientListScreen
import com.example.wtcchallenge.Screens.LoginScreen
import com.example.wtcchallenge.Screens.MessagesScreen
import com.example.wtcchallenge.Screens.ProfileScreen
import com.example.wtcchallenge.Screens.RegisterScreen
import com.example.wtcchallenge.Screens.SupportScreen
import com.example.wtcchallenge.composables.Screen
import com.example.wtcchallenge.composables.screens.CampaignScreen
import com.example.wtcchallenge.ui.theme.WTCChallengeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WTCChallengeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WTCApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun WTCApp(modifier: Modifier = Modifier) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Screen.Login.route, modifier = modifier) {

        composable(Screen.Login.route) {
            LoginScreen(
                onLogin = { nav.navigate(Screen.Messages.route) },
                onCadastrar = { nav.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onCadastroSucesso = {
                    nav.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onVoltar = { nav.popBackStack() }
            )
        }

        composable(Screen.Messages.route) {
            MessagesScreen(
                onProfileClick = { nav.navigate(Screen.Profile.route) },
                onCampaignClick = { nav.navigate(Screen.Campaign.route) },
                onMessagesClick = { nav.navigate(Screen.Messages.route) },
                onClientClick = { nav.navigate(Screen.Client.route) },
                onChatClick = { conversationId ->
                    nav.navigate(Screen.Chat.withId(conversationId))
                }
            )
        }

        composable(
            route = Screen.Chat.route,
            arguments = listOf(navArgument("conversationId") { type = NavType.StringType })
        ) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString("conversationId") ?: ""
            SupportScreen(
                conversationId = conversationId,
                onProfileClick = { nav.navigate(Screen.Profile.route) },
                onCampaignClick = { nav.navigate(Screen.Campaign.route) },
                onMessagesClick = { nav.navigate(Screen.Messages.route) },
                onClientClick = { nav.navigate(Screen.Client.route) }
            )
        }

        composable(Screen.Client.route) {
            ClientListScreen(
                onProfileClick = { nav.navigate(Screen.Profile.route) },
                onCampaignClick = { nav.navigate(Screen.Campaign.route) },
                onMessagesClick = { nav.navigate(Screen.Messages.route) },
                onClientClick = { nav.navigate(Screen.Client.route) }
            )
        }

        composable(Screen.Campaign.route) {
            CampaignScreen(
                onProfileClick = { nav.navigate(Screen.Profile.route) },
                onCampaignClick = { nav.navigate(Screen.Campaign.route) },
                onMessagesClick = { nav.navigate(Screen.Messages.route) },
                onClientClick = { nav.navigate(Screen.Client.route) }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onProfileClick = { nav.navigate(Screen.Profile.route) },
                onCampaignClick = { nav.navigate(Screen.Campaign.route) },
                onMessagesClick = { nav.navigate(Screen.Messages.route) },
                onClientClick = { nav.navigate(Screen.Client.route) }
            )
        }
    }
}
