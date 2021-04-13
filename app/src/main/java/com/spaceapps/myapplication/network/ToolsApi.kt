package com.spaceapps.myapplication.network

import com.spaceapps.myapplication.models.remote.tools.MetadataResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ToolsApi {

    @GET("/tools/link-metadata")
    suspend fun getLinkMetaData(@Query("url") link: String): MetadataResponse

    @GET("/tools/generate-qr-code")
    suspend fun generateQrCode(
        @Query("data") data: String,
        @Query("width") width: Int? = null,
        @Query("height") height: Int? = null
    ): String
}