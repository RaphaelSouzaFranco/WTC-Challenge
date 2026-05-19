package com.example.wtcchallenge.network.dto

data class ClientDto(
    val id: String = "",
    val nome: String = "",
    val numero: String = "",
    val ramo: String = "",
    val status: String = "",
    val tags: List<String> = emptyList(),
    val score: Int = 0,
    val operatorId: String = ""
)
