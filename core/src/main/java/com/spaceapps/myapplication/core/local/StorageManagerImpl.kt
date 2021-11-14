package com.spaceapps.myapplication.core.local

class StorageManagerImpl(
    private val db: SpaceAppsDatabase,
    private val dataStoreManager: DataStoreManager
) : StorageManager {
    override suspend fun clear() {
        db.clearAllTables()
        dataStoreManager.clearData()
    }

    override suspend fun getAccessToken(): String? = dataStoreManager.getAccessToken()

}