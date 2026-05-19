package com.example.wtcchallenge.network

object SessionManager {
    var authToken: String? = null
    var operatorId: String? = null
    var operatorName: String? = null

    fun isLoggedIn(): Boolean = authToken != null

    fun clear() {
        authToken = null
        operatorId = null
        operatorName = null
    }
}
