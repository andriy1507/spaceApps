package com.spaceapps.myapplication.core.network.calls

import com.spaceapps.myapplication.core.models.remote.PaginationResponse
import com.spaceapps.myapplication.core.models.remote.locations.LocationRequest
import com.spaceapps.myapplication.core.models.remote.locations.LocationResponse
import retrofit2.http.*

interface LocationsCalls {

    @GET("/locations")
    suspend fun getLocations(
        @Query("search") search: String? = null,
        @Query("size") size: Int?,
        @Query("page") page: Int?
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
