package com.spaceapps.myapplication.local

import androidx.datastore.core.DataStore
import com.spaceapps.myapplication.Settings
import com.spaceapps.myapplication.utils.ApplicationLanguage
import javax.inject.Inject

class SettingsStorage @Inject constructor(
    private val dataStore: DataStore<Settings>
) {

    suspend fun setNotificationsEnabled(enabled: Boolean) = dataStore.updateData {
        it.toBuilder()
            .setNotificationsEnabled(enabled)
            .build()
    }

    suspend fun setLanguage(@ApplicationLanguage language: String) = dataStore.updateData {
        it.toBuilder()
            .setLanguage(language)
            .build()
    }

}