package com.example.wtcchallenge.network.dto

data class RegisterRequestDto(
    val nome: String,
    val email: String,
    val senha: String,
    val cargo: String = "Operador"
)
