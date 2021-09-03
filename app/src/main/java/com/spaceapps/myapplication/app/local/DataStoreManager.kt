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

    suspend fun getAuthToken() = dataStore.data.first()[AUTH_TOKEN]

    suspend fun getRefreshToken() = dataStore.data.first()[REFRESH_TOKEN]
    suspend fun storeTokens(authToken: String, refreshToken: String) = dataStore.edit {
        it[AUTH_TOKEN] = authToken
        it[REFRESH_TOKEN] = refreshToken
    }

    suspend fun removeTokens() = dataStore.edit {
        it.remove(AUTH_TOKEN)
        it.remove(REFRESH_TOKEN)
    }

    suspend fun clearData() = dataStore.edit { it.clear() }

    companion object {
        private val AUTH_TOKEN = stringPreferencesKey("AUTH_TOKEN")
        private val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
    }
}
