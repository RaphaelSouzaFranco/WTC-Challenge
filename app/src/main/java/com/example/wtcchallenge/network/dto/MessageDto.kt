package com.example.wtcchallenge.network.dto

data class MessageDto(
    val id: String = "",
    val conversationId: String = "",
    val senderId: String = "",
    val senderType: String = "",
    val type: String = "TEXT",
    val content: String? = null,
    val mediaUrl: String? = null,
    val deeplinkUrl: String? = null,
    val deeplinkLabel: String? = null,
    val createdAt: String? = null
)
