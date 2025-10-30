package com.example.wtcchallenge.composables

// =========================================================
// 1️⃣ DATA CLASS – MODELO DE CLIENTE
// =========================================================
// Representa a estrutura dos dados de cada cliente
data class Cliente(
    val nome: String,
    val numero: String,
    val ramo: String,
    val status: String,
    val tags: List<String>,
    val score: Int
)
