package com.spaceapps.myapplication.app.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun getAccessToken() = dataStore.data.first()[ACCESS_TOKEN]

    suspend fun getRefreshToken() = dataStore.data.first()[REFRESH_TOKEN]

    suspend fun storeTokens(accessToken: String, refreshToken: String) = dataStore.edit {
        it[ACCESS_TOKEN] = accessToken
        it[REFRESH_TOKEN] = refreshToken
    }

    suspend fun removeTokens() = dataStore.edit {
        it.remove(ACCESS_TOKEN)
        it.remove(REFRESH_TOKEN)
    }

    suspend fun clearData() = dataStore.edit { it.clear() }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        private val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
    }
}
