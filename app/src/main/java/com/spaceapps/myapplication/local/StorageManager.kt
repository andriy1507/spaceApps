package com.spaceapps.myapplication.local

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
class StorageManager @Inject constructor(
    private val db: SpaceAppsDatabase,
    private val authTokenStorage: AuthTokenStorage,
    private val settingsStorage: SettingsStorage
) {

    suspend fun clear() = withContext(Dispatchers.IO) {
        db.clearAllTables()
        authTokenStorage.clear()
        settingsStorage.clear()
    }
}
