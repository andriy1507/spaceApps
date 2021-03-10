package com.spaceapps.myapplication.network

import com.spaceapps.myapplication.models.remote.PaginationResponse
import com.spaceapps.myapplication.models.remote.feeds.FeedRequest
import com.spaceapps.myapplication.models.remote.feeds.FeedResponse
import retrofit2.http.*

interface FeedsApi {

    @GET("/feeds")
    suspend fun getFeedsPagination(
        @Query("search") search: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): PaginationResponse<FeedResponse>

    @POST("/feeds/create")
    suspend fun createFeed(@Body request: FeedRequest): FeedResponse

    @PUT("/feeds/update/{feedId}")
    suspend fun updateFeed(@Path("feedId") feedId: Int, @Body request: FeedRequest)

    @DELETE("/feeds/delete/{feedId}")
    suspend fun deleteFeed(@Path("feedId") feedId: Int)
}
