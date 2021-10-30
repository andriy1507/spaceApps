package com.spaceapps.myapplication.core.models.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LocationRemoteKeys")
data class LocationRemoteKey(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name = "nextKey")
    val nextKey: Int?,
    @ColumnInfo(name = "prevKey")
    val prevKey: Int?,
    @ColumnInfo(name = "query")
    val query: String?
)
