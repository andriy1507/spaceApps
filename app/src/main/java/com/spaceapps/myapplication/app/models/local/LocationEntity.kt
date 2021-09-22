package com.spaceapps.myapplication.app.models.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "Locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "longitude")
    val longitude: Float,
    @ColumnInfo(name = "latitude")
    val latitude: Float,
    @ColumnInfo(name = "altitude")
    val altitude: Float,
    @ColumnInfo(name = "created")
    val created: LocalDateTime
)
