package com.spaceapps.myapplication.network

import com.spaceapps.myapplication.models.remote.PaginationResponse
import com.spaceapps.myapplication.models.remote.feeds.CommentRequest
import com.spaceapps.myapplication.models.remote.feeds.CommentResponse
import com.spaceapps.myapplication.models.remote.feeds.FeedRequest
import com.spaceapps.myapplication.models.remote.feeds.FeedResponse
import retrofit2.http.*

interface FeedsApi {

    @GET("/feeds")
    suspend fun getFeedsPaginated(
        @Query("search") search: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): PaginationResponse<FeedResponse>

    @POST("/feeds/create")
    suspend fun createFeed(@Body request: FeedRequest): FeedResponse

    @DELETE("/feeds/delete/{feedId}")
    suspend fun deleteFeed(@Path("feedId") feedId: Int)

    @PUT("/feeds/update/{feedId}")
    suspend fun updateFeed(@Path("feedId") feedId: Int, @Body request: FeedRequest)

    @PATCH("/feed/like/{feedId}")
    suspend fun toggleFeedLike(@Path("feedId") feedId: Int)

    @GET("/feed/comments/{feedId}")
    suspend fun getCommentsPaginated(
        @Path("feedId") feedId: Int,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): PaginationResponse<CommentResponse>

    @POST("/feed/comments/create/{feedId}")
    suspend fun createComment(
        @Path("feedId") feedId: Int,
        @Body request: CommentRequest
    ): CommentResponse

    @DELETE("/feed/comments/delete/{commentId}")
    suspend fun deleteComment(@Path("commentId") commentId: Int)

    @PUT("/feed/comments/update/{commentId}")
    suspend fun updateComment(
        @Path("commentId") commentId: Int,
        @Body request: CommentRequest
    ): CommentResponse

    @PATCH("/feed/comments/like/{commentId}")
    suspend fun toggleCommentLike(@Path("commentId") commentId: Int)
}
