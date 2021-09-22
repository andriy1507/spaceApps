package com.spaceapps.myapplication.app.network.calls

import com.spaceapps.myapplication.app.models.remote.PaginationResponse
import com.spaceapps.myapplication.app.models.remote.location.LocationRequest
import com.spaceapps.myapplication.app.models.remote.location.LocationResponse
import retrofit2.http.*

interface LocationsCalls {

    @GET("/locations")
    suspend fun getLocations(
        @Query("search") search: String? = null,
        @Query("pageSize") pageSize: Int?,
        @Query("pageIndex") page: Int?
    ): PaginationResponse<LocationResponse>

    @POST("/locations")
    suspend fun postLocation(@Body request: LocationRequest): LocationResponse

    @PUT("/locations/{id}")
    suspend fun updateLocation(
        @Path("id") id: Int,
        @Body request: LocationRequest
    ): LocationResponse

    @DELETE("/locations/{id}")
    suspend fun deleteLocation(@Path("id") id: Int)
}
