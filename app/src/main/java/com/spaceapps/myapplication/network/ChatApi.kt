package com.spaceapps.myapplication.network

import com.spaceapps.myapplication.models.remote.PaginationResponse
import com.spaceapps.myapplication.models.remote.chat.ConversationResponse
import com.spaceapps.myapplication.models.remote.chat.MessageRequest
import com.spaceapps.myapplication.models.remote.chat.MessageResponse
import retrofit2.http.*

interface ChatApi {

    @GET("/chat/conversations")
    suspend fun getConversationsPaginated(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("search") search: String? = null
    ): PaginationResponse<ConversationResponse>

    @POST("/chat/conversations/create")
    suspend fun addConversation(
        @Body conversation: ConversationResponse
    ): ConversationResponse

    @DELETE("/chat/conversations/delete/{conversationId}")
    suspend fun deleteConversation(
        @Path("conversationId") conversationId: String
    )

    @PUT("/chat/conversations/update/{conversationId}")
    suspend fun updateConversation(
        @Path("conversationId") conversationId: String,
        @Body conversation: ConversationResponse
    ): ConversationResponse

    @GET("/chat/messages/{conversationId}")
    suspend fun getMessagesPaginated(
        @Path("conversationId") conversationId: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("search") search: String? = null
    ): PaginationResponse<MessageResponse>

    @POST("/chat/messages/create/{conversationId}")
    suspend fun createMessage(
        @Path("conversationId") conversationId: String,
        @Body request: MessageRequest
    ): MessageResponse

    @DELETE("/chat/messages/delete/{messageId}")
    suspend fun deleteMessage(
        @Path("messageId") messageId: String
    )

    @PUT("/chat/messages/update/{messageId}")
    suspend fun updateMessage(
        @Path("messageId") messageId: String,
        @Body request: MessageRequest
    ): MessageResponse
}
