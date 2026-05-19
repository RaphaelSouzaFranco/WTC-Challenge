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
    data object ClientTimeline : Screen("client/{clientId}/timeline") {
        fun withId(id: String) = "client/$id/timeline"
    }
    data object Inbox : Screen("inbox/{customerId}") {
        fun withId(id: String) = "inbox/$id"
    }
    data object Segments : Screen("segments")
    data object Profile : Screen("profile")
    data object ClientProfile : Screen("client/{clientId}/profile") {
        fun withId(id: String) = "client/$id/profile"
    }
}