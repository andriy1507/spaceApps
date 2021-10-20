package com.spaceapps.myapplication.app.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.spaceapps.myapplication.app.DEGREES_DMS
import com.spaceapps.myapplication.app.SYSTEM_GEO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun getAccessToken() = dataStore.data.first()[ACCESS_TOKEN]

    suspend fun getRefreshToken() = dataStore.data.first()[REFRESH_TOKEN]

    fun observeDegreesFormat() = dataStore.data.map { it[DEGREES_FORMAT] ?: DEGREES_DMS }

    fun observeCoordSystem() = dataStore.data.map { it[COORD_SYSTEM] ?: SYSTEM_GEO }

    suspend fun storeTokens(accessToken: String, refreshToken: String) = dataStore.edit {
        it[ACCESS_TOKEN] = accessToken
        it[REFRESH_TOKEN] = refreshToken
    }

    suspend fun setCoordinatesSystem(system: String) = dataStore.edit {
        it[COORD_SYSTEM] = system
    }

    suspend fun setDegreesFormat(format: String) = dataStore.edit {
        it[DEGREES_FORMAT] = format
    }

    suspend fun removeTokens() = dataStore.edit {
        it.remove(ACCESS_TOKEN)
        it.remove(REFRESH_TOKEN)
    }

    suspend fun clearData() = dataStore.edit { it.clear() }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        private val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
        private val COORD_SYSTEM = stringPreferencesKey("COORD_SYSTEM")
        private val DEGREES_FORMAT = stringPreferencesKey("DEGREES_FORMAT")
    }
}
