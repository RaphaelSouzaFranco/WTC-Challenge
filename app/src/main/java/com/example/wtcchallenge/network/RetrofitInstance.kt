package com.example.wtcchallenge.network

import com.example.wtcchallenge.network.dto.RefreshRequestDto
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val gson = Gson()

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val builder = chain.request().newBuilder()
            SessionManager.authToken?.let { token ->
                builder.header("Authorization", "Bearer $token")
            }
            val response = chain.proceed(builder.build())

            if (response.code == 401 && SessionManager.refreshToken != null) {
                response.close()
                val refreshed = tryRefreshToken()
                if (refreshed) {
                    val newBuilder = chain.request().newBuilder()
                    SessionManager.authToken?.let { token ->
                        newBuilder.header("Authorization", "Bearer $token")
                    }
                    return@addInterceptor chain.proceed(newBuilder.build())
                }
            }
            response
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun tryRefreshToken(): Boolean {
        return try {
            val refreshBody = gson.toJson(RefreshRequestDto(SessionManager.refreshToken ?: ""))
            val request = Request.Builder()
                .url("${BASE_URL}api/auth/refresh")
                .post(refreshBody.toRequestBody("application/json".toMediaType()))
                .build()
            val refreshClient = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build()
            val response = refreshClient.newCall(request).execute()
            if (response.isSuccessful) {
                val body = response.body?.string()
                val loginResponse = gson.fromJson(body, com.example.wtcchallenge.network.dto.LoginResponseDto::class.java)
                SessionManager.authToken = loginResponse.token
                SessionManager.refreshToken = loginResponse.refreshToken
                true
            } else {
                SessionManager.clear()
                false
            }
        } catch (_: Exception) {
            false
        }
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}