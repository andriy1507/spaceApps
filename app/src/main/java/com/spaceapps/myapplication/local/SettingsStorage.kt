package com.spaceapps.myapplication.local

import androidx.datastore.core.DataStore
import com.spaceapps.myapplication.Settings
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsStorage @Inject constructor(
    private val dataStore: DataStore<Settings>
) {
    suspend fun getLanguage() = dataStore.data.firstOrNull()?.language ?: Settings.Language.English

    suspend fun setLanguage(language: Settings.Language) = dataStore.updateData {
        it.toBuilder()
            .setLanguage(language)
            .build()
    }

    suspend fun clear() = dataStore.updateData {
        it.toBuilder().clear().build()
    }
}
