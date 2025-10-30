package com.example.wtcchallenge.network

import com.example.wtcchallenge.composables.Client
import retrofit2.http.*


interface ApiService {

    // GET - Buscar todos os clientes
    @GET("clients")
    suspend fun getClients(): List<Client>

    // GET - Buscar cliente por ID
    @GET("clients/{id}")
    suspend fun getClientById(@Path("id") id: String): Client

    // POST - Criar novo cliente
    @POST("clients")
    suspend fun createClient(@Body client: Client): Client

    // PUT - Atualizar cliente
    @PUT("clients/{id}")
    suspend fun updateClient(@Path("id") id: String, @Body client: Client): Client

    // DELETE - Deletar cliente
    @DELETE("clients/{id}")
    suspend fun deleteClient(@Path("id") id: String): Client
}