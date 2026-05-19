package com.example.wtcchallenge.network.dto

data class ConversationDto(
    val id: String = "",
    val clientId: String = "",
    val operatorId: String = "",
    val client: ClientDto? = null,
    val lastMessage: String? = null,
    val lastMessageAt: String? = null,
    val unreadCount: Int = 0,
    val isGroup: Boolean = false
)
