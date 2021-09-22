package com.spaceapps.myapplication.app.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.spaceapps.myapplication.app.local.dao.LocationsDao
import com.spaceapps.myapplication.app.local.dao.LocationsRemoteKeyDao
import com.spaceapps.myapplication.app.models.local.LocationEntity
import com.spaceapps.myapplication.app.models.local.LocationRemoteKey

@Database(
    version = 1,
    entities = [LocationEntity::class, LocationRemoteKey::class],
    exportSchema = true
)
@TypeConverters(RoomTypeConverters::class)
abstract class SpaceAppsDatabase : RoomDatabase() {

    abstract fun getLocationsDao(): LocationsDao

    abstract fun getLocationsRemoteKeyDao(): LocationsRemoteKeyDao
}
