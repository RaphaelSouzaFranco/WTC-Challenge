package com.example.wtcchallenge.model

data class Message(
    val id: String = "",
    val conversationId: String = "",
    val senderId: String = "",
    val senderType: String = "",
    val type: String = "TEXT",
    val content: String? = null,
    val mediaUrl: String? = null,
    val mediaType: String? = null,
    val deeplinkUrl: String? = null,
    val deeplinkLabel: String? = null,
    val readAt: String? = null,
    val createdAt: String? = null
)
