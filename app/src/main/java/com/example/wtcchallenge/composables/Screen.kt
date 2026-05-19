package com.example.wtcchallenge.composables

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Messages : Screen("messages")
    data object Chat : Screen("chat/{conversationId}") {
        fun withId(id: String) = "chat/$id"
    }
    data object Campaign : Screen("campaign")
    data object Client : Screen("client")
    data object Profile : Screen("profile")
}