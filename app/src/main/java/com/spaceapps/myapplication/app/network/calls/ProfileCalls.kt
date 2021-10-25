package com.spaceapps.myapplication.app.network.calls

import com.spaceapps.myapplication.app.models.remote.PaginationResponse
import com.spaceapps.myapplication.app.models.remote.profile.DeviceResponse
import com.spaceapps.myapplication.app.models.remote.profile.ProfileRequest
import com.spaceapps.myapplication.app.models.remote.profile.ProfileResponse
import retrofit2.http.*

interface ProfileCalls {

    @GET("/profile")
    suspend fun getProfile(): ProfileResponse

    @PUT("/profile")
    suspend fun updateProfile(@Body request: ProfileRequest): ProfileResponse

    @GET("/profile/devices")
    suspend fun getProfileDevices(
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): PaginationResponse<DeviceResponse>

    @DELETE("/profile/devices/{id}")
    suspend fun deleteProfileDeviceById(@Path("id") id: Int)
}
