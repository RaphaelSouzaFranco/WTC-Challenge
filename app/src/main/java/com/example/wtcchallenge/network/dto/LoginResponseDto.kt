package com.example.wtcchallenge.network.dto

import com.example.wtcchallenge.model.Operator

data class LoginResponseDto(
    val token: String = "",
    val refreshToken: String? = null,
    val tokenType: String = "Bearer",
    val operator: Operator = Operator()
)
