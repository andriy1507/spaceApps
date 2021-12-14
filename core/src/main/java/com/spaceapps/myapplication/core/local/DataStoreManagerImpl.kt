package com.spaceapps.myapplication.core.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.spaceapps.myapplication.core.DEGREES_DMS
import com.spaceapps.myapplication.core.PREFERENCES_DATA_STORE
import com.spaceapps.myapplication.core.SYSTEM_GEO
import com.spaceapps.myapplication.core.models.remote.auth.AuthTokenResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.format.DateTimeFormatter

class DataStoreManagerImpl(context: Context) : DataStoreManager {

    private val Context.preferencesDataStore by preferencesDataStore(PREFERENCES_DATA_STORE)

    private val dataStore = context.preferencesDataStore

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override suspend fun getAccessToken() = dataStore.data.first()[ACCESS_TOKEN]

    override suspend fun getRefreshToken() = dataStore.data.first()[REFRESH_TOKEN]

    override fun observeDegreesFormat() = dataStore.data.map { it[DEGREES_FORMAT] ?: DEGREES_DMS }

    override fun observeCoordSystem() = dataStore.data.map { it[COORD_SYSTEM] ?: SYSTEM_GEO }

    override suspend fun storeTokens(response: AuthTokenResponse) {
        dataStore.edit {
            it[ACCESS_TOKEN] = response.accessToken
            it[REFRESH_TOKEN] = response.refreshToken
            it[ACCESS_TOKEN_EXP] = response.accessTokenExp.format(formatter)
            it[REFRESH_TOKEN_EXP] = response.refreshTokenExp.format(formatter)
        }
    }

    override suspend fun setCoordinatesSystem(system: String) {
        dataStore.edit { it[COORD_SYSTEM] = system }
    }

    override suspend fun setDegreesFormat(format: String) {
        dataStore.edit { it[DEGREES_FORMAT] = format }
    }

    override suspend fun removeTokens() {
        dataStore.edit {
            it.remove(ACCESS_TOKEN)
            it.remove(REFRESH_TOKEN)
        }
    }

    override suspend fun clearData() {
        dataStore.edit { it.clear() }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        private val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
        private val ACCESS_TOKEN_EXP = stringPreferencesKey("ACCESS_TOKEN_EXP")
        private val REFRESH_TOKEN_EXP = stringPreferencesKey("REFRESH_TOKEN_EXP")
        private val COORD_SYSTEM = stringPreferencesKey("COORD_SYSTEM")
        private val DEGREES_FORMAT = stringPreferencesKey("DEGREES_FORMAT")
    }
}
