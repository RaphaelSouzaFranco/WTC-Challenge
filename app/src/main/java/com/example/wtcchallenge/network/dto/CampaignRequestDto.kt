package com.example.wtcchallenge.network.dto

data class CampaignRequestDto(
    val titulo: String,
    val mensagem: String,
    val targetAudience: String = "Simple",
    val segmentId: String? = null,
    val operatorId: String
)
