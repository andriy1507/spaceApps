package com.spaceapps.myapplication.network

import retrofit2.http.*

interface NotificationsApi {

    @GET("/notifications")
    suspend fun getNotificationsPaginated(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    )

    @DELETE("/notifications/{notificationId}")
    suspend fun deleteNotification(@Path("notificationId") notificationId: Int)

    @PATCH("/notifications/{notificationId}/mark-as-viewed")
    suspend fun viewNotification(@Path("notificationId") notificationId: Int)
}
