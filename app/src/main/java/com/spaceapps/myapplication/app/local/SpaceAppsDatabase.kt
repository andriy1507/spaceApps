package com.spaceapps.myapplication.app.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.spaceapps.myapplication.app.local.dao.*
import com.spaceapps.myapplication.app.models.local.*

@Database(
    version = 2,
    entities = [
        LocationEntity::class,
        LocationRemoteKey::class,
        NotificationEntity::class,
        NotificationRemoteKey::class,
        DeviceEntity::class,
        DeviceRemoteKey::class
    ],
    exportSchema = true
)
@TypeConverters(RoomTypeConverters::class)
abstract class SpaceAppsDatabase : RoomDatabase() {

    abstract fun getLocationsDao(): LocationsDao

    abstract fun getLocationsRemoteKeyDao(): LocationsRemoteKeysDao

    abstract fun getNotificationsDao(): NotificationsDao

    abstract fun getNotificationsRemoteKeyDao(): NotificationsRemoteKeysDao

    abstract fun getDevicesDao(): DevicesDao

    abstract fun getDevicesRemoteKeysDao(): DevicesRemoteKeysDao
}
