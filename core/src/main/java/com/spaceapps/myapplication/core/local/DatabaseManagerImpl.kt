package com.spaceapps.myapplication.core.local

class DatabaseManagerImpl(private val db: SpaceAppsDatabase) : DatabaseManager {

    override suspend fun clear() = db.clearAllTables()
}
