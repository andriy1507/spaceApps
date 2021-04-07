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

    @POST("/feeds")
    suspend fun createFeed(@Body request: FeedRequest): FeedFullResponse

    @DELETE("/feeds/{feedId}")
    suspend fun deleteFeed(@Path("feedId") feedId: Int)

    @PUT("/feeds/{feedId}")
    suspend fun updateFeed(
        @Path("feedId") feedId: Int,
        @Body request: FeedRequest
    )

    @PATCH("/feeds/{feedId}/toggle-like")
    suspend fun toggleFeedLike(@Path("feedId") feedId: Int)

    @PATCH("/feeds/{feedId}/toggle-saved")
    suspend fun toggleFeedSaved(@Path("feedId") feedId: Int)

    @GET("/feeds/{feedId}/comments")
    suspend fun getCommentsPaginated(
        @Path("feedId") feedId: Int,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): PaginationResponse<CommentResponse>

    @POST("/feeds/{feedId}/comments")
    suspend fun createComment(
        @Path("feedId") feedId: Int,
        @Body request: CommentRequest
    ): CommentResponse

    @DELETE("/feeds/{feedId}/comments/{commentId}")
    suspend fun deleteComment(
        @Path("feedId") feedId: Int,
        @Path("commentId") commentId: Int
    )

    @PUT("/feeds/{feedId}/comments/{commentId}")
    suspend fun updateComment(
        @Path("feedId") feedId: Int,
        @Path("commentId") commentId: Int,
        @Body request: CommentRequest
    ): CommentResponse

    @PATCH("/feeds/{feedId}/comments/{commentId}/toggle-like")
    suspend fun toggleCommentLike(
        @Path("feedId") feedId: Int,
        @Path("commentId") commentId: Int
    )
}
