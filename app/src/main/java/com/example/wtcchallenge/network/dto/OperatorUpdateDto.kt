package com.example.wtcchallenge.network.dto

data class OperatorUpdateDto(
    val nome: String,
    val email: String,
    val cargo: String,
    val notas: String?,
    val darkMode: Boolean
)
