package com.spaceapps.myapplication.core.local

import com.spaceapps.myapplication.core.models.remote.auth.AuthTokenResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class TestDataStoreManager : DataStoreManager {

    override suspend fun getAccessToken(): String? = null

    override suspend fun getRefreshToken(): String? = null

    override fun observeDegreesFormat(): Flow<String> = emptyFlow()

    override fun observeCoordSystem(): Flow<String> = emptyFlow()

    override suspend fun storeTokens(response: AuthTokenResponse) = Unit

    override suspend fun setCoordinatesSystem(system: String) = Unit

    override suspend fun setDegreesFormat(format: String) = Unit

    override suspend fun removeTokens() = Unit

    override suspend fun clearData() = Unit
}
