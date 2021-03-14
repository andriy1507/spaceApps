package com.spaceapps.myapplication.network

import com.spaceapps.myapplication.models.remote.PaginationResponse
import com.spaceapps.myapplication.models.remote.feeds.*
import retrofit2.http.*

interface FeedsApi {

    @GET("/feeds")
    suspend fun getFeedsPaginated(
        @Query("search") search: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): PaginationResponse<FeedShortResponse>

    @GET("/feeds/{feedId}")
    suspend fun getFeedById(@Path("feedId") feedId: Int): FeedFullResponse

    @POST("/feeds/create")
    suspend fun createFeed(@Body request: FeedRequest): FeedFullResponse

    @DELETE("/feeds/delete/{feedId}")
    suspend fun deleteFeed(@Path("feedId") feedId: Int)

    @PUT("/feeds/update/{feedId}")
    suspend fun updateFeed(@Path("feedId") feedId: Int, @Body request: FeedRequest)

    @PATCH("/feeds/like/{feedId}")
    suspend fun toggleFeedLike(@Path("feedId") feedId: Int)

    @GET("/feeds/comments/{feedId}")
    suspend fun getCommentsPaginated(
        @Path("feedId") feedId: Int,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): PaginationResponse<CommentResponse>

    @POST("/feeds/comments/create/{feedId}")
    suspend fun createComment(
        @Path("feedId") feedId: Int,
        @Body request: CommentRequest
    ): CommentResponse

    @DELETE("/feeds/comments/delete/{commentId}")
    suspend fun deleteComment(@Path("commentId") commentId: Int)

    @PUT("/feeds/comments/update/{commentId}")
    suspend fun updateComment(
        @Path("commentId") commentId: Int,
        @Body request: CommentRequest
    ): CommentResponse

    @PATCH("/feeds/comments/like/{commentId}")
    suspend fun toggleCommentLike(@Path("commentId") commentId: Int)
}
