package com.example.wtcchallenge.model

data class Client(
    val id: String = "",
    val nome: String = "",
    val numero: String = "",
    val ramo: String = "",
    val status: String = "",
    val tags: List<String> = emptyList(),
    val score: Int = 0,
    val operatorId: String = "",
    val createdAt: String? = null,
    val updatedAt: String? = null
)
