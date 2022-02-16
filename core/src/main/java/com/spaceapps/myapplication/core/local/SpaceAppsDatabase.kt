package com.spaceapps.myapplication.core.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.spaceapps.myapplication.core.local.dao.*
import com.spaceapps.myapplication.core.models.local.devices.DeviceEntity
import com.spaceapps.myapplication.core.models.local.devices.DeviceRemoteKey
import com.spaceapps.myapplication.core.models.local.locations.LocationEntity
import com.spaceapps.myapplication.core.models.local.locations.LocationRemoteKey
import com.spaceapps.myapplication.core.models.local.notifications.NotificationEntity
import com.spaceapps.myapplication.core.models.local.notifications.NotificationRemoteKey

@Database(
    version = 1,
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

    abstract fun getLocationsRemoteKeysDao(): LocationsRemoteKeysDao

    abstract fun getNotificationsDao(): NotificationsDao

    abstract fun getNotificationsRemoteKeysDao(): NotificationsRemoteKeysDao

    abstract fun getDevicesDao(): DevicesDao

    abstract fun getDevicesRemoteKeysDao(): DevicesRemoteKeysDao
}
