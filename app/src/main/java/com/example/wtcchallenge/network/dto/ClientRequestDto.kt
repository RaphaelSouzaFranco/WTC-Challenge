package com.example.wtcchallenge.network.dto

data class ClientRequestDto(
    val nome: String,
    val numero: String,
    val ramo: String = "",
    val status: String = "Lead",
    val tags: List<String> = emptyList(),
    val score: Int = 0,
    val operatorId: String
)
