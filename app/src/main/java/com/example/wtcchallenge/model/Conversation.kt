package com.example.wtcchallenge.model

data class Conversation(
    val id: String = "",
    val clientId: String = "",
    val operatorId: String = "",
    val client: Client? = null,
    val lastMessage: String? = null,
    val lastMessageAt: String? = null,
    val unreadCount: Int = 0,
    val isGroup: Boolean = false,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
