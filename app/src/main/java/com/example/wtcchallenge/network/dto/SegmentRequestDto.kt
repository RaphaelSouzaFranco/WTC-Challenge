package com.example.wtcchallenge.network.dto

data class SegmentRequestDto(
    val nome: String,
    val descricao: String? = null,
    val operatorId: String,
    val criterios: Map<String, Any>? = null,
    val clientIds: List<String>? = null
)
