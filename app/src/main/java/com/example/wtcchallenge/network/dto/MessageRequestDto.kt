package com.example.wtcchallenge.network.dto

data class MessageRequestDto(
    val senderId: String,
    val senderType: String = "OPERATOR",
    val content: String,
    val type: String = "TEXT"
)
