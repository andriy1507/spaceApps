package com.spaceapps.myapplication.local

import androidx.datastore.core.DataStore
import com.spaceapps.myapplication.ENGLISH
import com.spaceapps.myapplication.Settings
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsStorage @Inject constructor(
    private val dataStore: DataStore<Settings>
) {

    val languageFlow = dataStore.data.map { it.language }

    val notificationsEnabledFlow = dataStore.data.map { it.notificationsEnabled }

    suspend fun getNotificationsEnabled() = dataStore.data.firstOrNull()?.notificationsEnabled ?: true

    suspend fun getLanguage() = dataStore.data.firstOrNull()?.language ?: ENGLISH

    suspend fun setNotificationsEnabled(enabled: Boolean) = dataStore.updateData {
        it.toBuilder()
            .setNotificationsEnabled(enabled)
            .build()
    }

    suspend fun setLanguage(language: String) = dataStore.updateData {
        it.toBuilder()
            .setLanguage(language)
            .build()
    }
}
