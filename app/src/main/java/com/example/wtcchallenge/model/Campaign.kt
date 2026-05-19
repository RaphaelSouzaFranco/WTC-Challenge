package com.example.wtcchallenge.model

data class Campaign(
    val id: String = "",
    val titulo: String = "",
    val mensagem: String = "",
    val targetAudience: String = "",
    val mediaUrl: String? = null,
    val status: String = "",
    val operatorId: String = "",
    val sentAt: String? = null,
    val createdAt: String? = null
)
