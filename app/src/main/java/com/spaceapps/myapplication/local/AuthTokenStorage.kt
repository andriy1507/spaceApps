package com.spaceapps.myapplication.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.spaceapps.myapplication.BuildConfig
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenStorage @Inject constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val AUTH_TOKEN = stringPreferencesKey("${BuildConfig.APPLICATION_ID}.AUTH_TOKEN")
        private val REFRESH_TOKEN =
            stringPreferencesKey("${BuildConfig.APPLICATION_ID}.REFRESH_TOKEN")
    }

    suspend fun getAuthToken() = dataStore.data.first()[AUTH_TOKEN]
    suspend fun getRefreshToken() = dataStore.data.first()[REFRESH_TOKEN]

    suspend fun storeTokens(authToken: String, refreshToken: String) = dataStore.edit {
        it[AUTH_TOKEN] = authToken
        it[REFRESH_TOKEN] = refreshToken
    }

    suspend fun clear() = dataStore.edit { it.clear() }
}
