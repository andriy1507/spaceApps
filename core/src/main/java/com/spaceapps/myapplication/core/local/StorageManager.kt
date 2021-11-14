package com.spaceapps.myapplication.core.local

interface StorageManager {

    suspend fun clear()

    suspend fun getAccessToken(): String?
}