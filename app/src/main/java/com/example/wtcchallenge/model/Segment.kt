package com.example.wtcchallenge.model

data class Segment(
    val id: String = "",
    val nome: String = "",
    val descricao: String? = null,
    val operatorId: String = "",
    val criterios: Map<String, Any>? = null,
    val clientIds: List<String>? = null,
    val clientCount: Int = 0,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
