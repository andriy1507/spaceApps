package com.spaceapps.myapplication.app.models.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NotificationRemoteKeys")
data class NotificationRemoteKey(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name = "nextKey")
    val nextKey: Int?,
    @ColumnInfo(name = "prevKey")
    val prevKey: Int?
)
