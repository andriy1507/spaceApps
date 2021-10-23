package com.spaceapps.myapplication.app.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Devices")
data class DeviceEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0
)
