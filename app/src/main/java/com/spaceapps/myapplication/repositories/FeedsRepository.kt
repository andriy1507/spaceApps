package com.spaceapps.myapplication.repositories

import com.spaceapps.myapplication.models.remote.PaginationResponse
import com.spaceapps.myapplication.models.remote.feeds.FeedRequest
import com.spaceapps.myapplication.models.remote.feeds.FeedResponse
import com.spaceapps.myapplication.network.FeedsApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedsRepository @Inject constructor(
    private val feedsApi: FeedsApi
) {

    suspend fun getFeedsPagination(
        search: String? = null,
        page: Int? = null,
        size: Int? = null
    ): PaginationResponse<FeedResponse> = feedsApi.getFeedsPagination(search, page, size)

    suspend fun createFeed(feed: FeedRequest) = feedsApi.createFeed(feed)

    suspend fun updateFeed(feedId: Int, request: FeedRequest) = feedsApi.updateFeed(feedId, request)

    suspend fun deleteFeed(feedId: Int) = feedsApi.deleteFeed(feedId)
}
