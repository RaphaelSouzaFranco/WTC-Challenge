package com.example.wtcchallenge.network.dto

data class OperatorDto(
    val id: String = "",
    val nome: String = "",
    val email: String = "",
    val cargo: String = "",
    val avatarUrl: String? = null,
    val notas: String? = null,
    val darkMode: Boolean = false
)
