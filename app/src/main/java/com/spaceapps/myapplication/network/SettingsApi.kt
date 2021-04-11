package com.spaceapps.myapplication.network

import retrofit2.http.PUT
import retrofit2.http.Path

interface SettingsApi {

    @PUT("/settings/set-language/{lang}")
    suspend fun setLanguage(@Path("path") language: String)
}