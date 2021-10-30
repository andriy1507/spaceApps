package com.spaceapps.myapplication.core.models.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "Notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "text")
    val text: String,
    @ColumnInfo(name = "created")
    val created: LocalDateTime,
    @ColumnInfo(name = "is_viewed")
    val isViewed: Boolean
)
