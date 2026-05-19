package com.example.wtcchallenge.model

data class Campaign(
    val id: String = "",
    val titulo: String = "",
    val mensagem: String = "",
    val targetAudience: String = "",
    val segmentId: String? = null,
    val mediaUrl: String? = null,
    val status: String = "",
    val operatorId: String = "",
    val variantOf: String? = null,
    val variantLabel: String? = null,
    val scheduledAt: String? = null,
    val sentAt: String? = null,
    val createdAt: String? = null
)
