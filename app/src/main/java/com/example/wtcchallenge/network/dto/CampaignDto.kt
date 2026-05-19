package com.example.wtcchallenge.network.dto

data class CampaignDto(
    val id: String = "",
    val titulo: String = "",
    val mensagem: String = "",
    val targetAudience: String = "",
    val mediaUrl: String? = null,
    val status: String = "",
    val operatorId: String = ""
)
