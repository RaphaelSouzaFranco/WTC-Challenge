package com.example.wtcchallenge.network

import com.example.wtcchallenge.model.Campaign
import com.example.wtcchallenge.model.Client
import com.example.wtcchallenge.model.Conversation
import com.example.wtcchallenge.model.Message
import com.example.wtcchallenge.model.Operator
import com.example.wtcchallenge.network.dto.CampaignRequestDto
import com.example.wtcchallenge.network.dto.LoginRequestDto
import com.example.wtcchallenge.network.dto.LoginResponseDto
import com.example.wtcchallenge.network.dto.MessageRequestDto
import com.example.wtcchallenge.network.dto.PageDto
import com.example.wtcchallenge.network.dto.RegisterRequestDto
import retrofit2.http.*

interface ApiService {

    // ── AUTH ─────────────────────────────────────────────────────────────────

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): Map<String, String>

    // ── CONVERSATIONS ─────────────────────────────────────────────────────────

    @GET("api/conversations")
    suspend fun getConversations(
        @Query("operatorId") operatorId: String,
        @Query("filter") filter: String = "Todos"
    ): List<Conversation>

    // ── MESSAGES ──────────────────────────────────────────────────────────────

    @GET("api/messages/{conversationId}")
    suspend fun getMessages(
        @Path("conversationId") conversationId: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 30
    ): PageDto<Message>

    @POST("api/messages/{conversationId}")
    suspend fun sendMessage(
        @Path("conversationId") conversationId: String,
        @Body request: MessageRequestDto
    ): Message

    // ── CLIENTS ───────────────────────────────────────────────────────────────

    @GET("api/clients")
    suspend fun getClients(
        @Query("search") search: String? = null,
        @Query("status") status: String? = null,
        @Query("minScore") minScore: Int? = null,
        @Query("maxScore") maxScore: Int? = null,
        @Query("tag") tag: String? = null
    ): List<Client>

    @GET("api/clients/{id}")
    suspend fun getClientById(@Path("id") id: String): Client

    // ── CAMPAIGNS ─────────────────────────────────────────────────────────────

    @GET("api/campaigns")
    suspend fun getCampaigns(@Query("operatorId") operatorId: String): List<Campaign>

    @POST("api/campaigns")
    suspend fun createCampaign(@Body request: CampaignRequestDto): Campaign

    @POST("api/campaigns/{id}/send")
    suspend fun sendCampaign(@Path("id") id: String): Campaign

    // ── OPERATORS ─────────────────────────────────────────────────────────────

    @GET("api/operators/{id}")
    suspend fun getOperator(@Path("id") id: String): Operator

    @PUT("api/operators/{id}")
    suspend fun updateOperator(@Path("id") id: String, @Body operator: Operator): Operator
}
