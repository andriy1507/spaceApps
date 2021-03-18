package com.spaceapps.myapplication.network

import com.spaceapps.myapplication.models.remote.PaginationResponse
import com.spaceapps.myapplication.models.remote.chat.ChatRequest
import com.spaceapps.myapplication.models.remote.chat.ChatResponse
import com.spaceapps.myapplication.models.remote.chat.MessageRequest
import com.spaceapps.myapplication.models.remote.chat.MessageResponse
import retrofit2.http.*

interface ChatApi {

    @GET("/chats")
    suspend fun getChatsPaginated(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("search") search: String? = null
    ): PaginationResponse<ChatResponse>

    @POST("/chats")
    suspend fun addChat(
        @Body chat: ChatRequest
    ): ChatResponse

    @DELETE("/chats/{conversationId}")
    suspend fun deleteChat(
        @Path("conversationId") conversationId: String
    )

    @PUT("/chats/{chatId}")
    suspend fun updateChat(
        @Path("chatId") chatId: String,
        @Body chat: ChatRequest
    ): ChatResponse

    @GET("/chats/{chatId}/messages")
    suspend fun getMessagesPaginated(
        @Path("chatId") chatId: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("search") search: String? = null
    ): PaginationResponse<MessageResponse>

    @POST("/chats/{chatId}/messages")
    suspend fun createMessage(
        @Path("chatId") chatId: String,
        @Body request: MessageRequest
    ): MessageResponse

    @DELETE("/chats/{chatId}/messages/{messageId}")
    suspend fun deleteMessage(
        @Path("chatId") chatId: String,
        @Path("messageId") messageId: String
    )

    @PUT("/chats/{chatId}/messages/{messageId}")
    suspend fun updateMessage(
        @Path("chatId") chatId: String,
        @Path("messageId") messageId: String,
        @Body request: MessageRequest
    ): MessageResponse
}
