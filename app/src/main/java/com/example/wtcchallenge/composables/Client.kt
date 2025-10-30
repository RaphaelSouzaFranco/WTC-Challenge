package com.example.wtcchallenge.composables

// =========================================================
// 1️⃣ DATA CLASS – MODELO DE CLIENTE
// =========================================================
// Representa a estrutura dos dados de cada cliente
data class Client(
    val nome: String,
    val numero: String,
    val ramo: String,
    val status: String,
    val tags: String,
    val score: Int
)
