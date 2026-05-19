package com.example.wtcchallenge.model

data class Operator(
    val id: String = "",
    val nome: String = "",
    val email: String = "",
    val cargo: String = "",
    val avatarUrl: String? = null,
    val darkMode: Boolean = false,
    val notas: String? = null,
    val createdAt: String? = null
)
