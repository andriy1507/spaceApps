package com.spaceapps.myapplication.core.local

import com.spaceapps.myapplication.core.models.remote.auth.AuthTokenResponse
import kotlinx.coroutines.flow.Flow

interface DataStoreManager {

    suspend fun getAccessToken(): String?

    suspend fun getRefreshToken(): String?

    fun observeDegreesFormat(): Flow<String>

    fun observeCoordSystem(): Flow<String>

    suspend fun storeTokens(response: AuthTokenResponse)

    suspend fun setCoordinatesSystem(system: String)

    suspend fun setDegreesFormat(format: String)

    suspend fun removeTokens()

    suspend fun clearData()
}
