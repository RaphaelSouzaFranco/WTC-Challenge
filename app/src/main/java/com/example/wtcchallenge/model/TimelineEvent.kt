package com.example.wtcchallenge.model

data class TimelineEvent(
    val id: String = "",
    val type: String = "",
    val description: String? = null,
    val data: Any? = null,
    val timestamp: String? = null
)
