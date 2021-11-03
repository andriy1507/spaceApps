package com.spaceapps.myapplication.core.network.calls

import com.spaceapps.myapplication.core.models.remote.PaginationResponse
import com.spaceapps.myapplication.core.models.remote.files.FileResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface FilesCalls {

    @GET("/files")
    suspend fun getFiles(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): PaginationResponse<FileResponse>

    @Multipart
    @POST("/files")
    suspend fun uploadFile(@Part("file") file: MultipartBody.Part)

    @GET("/files/{fileName}")
    suspend fun downloadFile(@Path("fileName") fileName: String): ResponseBody

    @DELETE("/files/{fileName}")
    suspend fun deleteFile(@Path("fileName") fileName: String)

}