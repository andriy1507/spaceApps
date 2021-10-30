package com.spaceapps.myapplication.core.network.calls

import com.spaceapps.myapplication.core.models.remote.PaginationResponse
import com.spaceapps.myapplication.core.models.remote.notifications.NotificationFullResponse
import com.spaceapps.myapplication.core.models.remote.notifications.NotificationShortResponse
import retrofit2.http.*

interface NotificationsCalls {

    @GET("/notifications")
    suspend fun getNotifications(
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): PaginationResponse<NotificationShortResponse>

    @GET("/notifications/{id}")
    suspend fun getNotificationById(@Path("id") id: Int): NotificationFullResponse

    @DELETE("/notifications/{id}")
    suspend fun deleteNotification(@Path("id") id: Int)

    @PATCH("/notifications/{id}/view")
    suspend fun updateNotificationViewed(@Path("id") id: Int)
}
