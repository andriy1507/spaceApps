package com.spaceapps.myapplication.network

import com.spaceapps.myapplication.models.remote.uploads.UploadResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface UploadsApi {

    @GET("/uploads/{fileName}")
    suspend fun getFile(@Path("fileName") fileName: String): ResponseBody

    @Multipart
    @POST("/uploads/image")
    suspend fun uploadImage(@Part("file") file: MultipartBody.Part): UploadResponse

    @Multipart
    @POST("/uploads/video")
    suspend fun uploadVideo(@Part("file") file: MultipartBody.Part): UploadResponse

    @Multipart
    @POST("/uploads/audio")
    suspend fun uploadAudio(@Part("file") file: MultipartBody.Part): UploadResponse

    @Multipart
    @POST("/uploads/file")
    suspend fun uploadFile(@Part("file") file: MultipartBody.Part): UploadResponse

    @DELETE("/uploads/delete/{fileName}")
    suspend fun deleteFile(@Path("fileName") fileName: String)
}