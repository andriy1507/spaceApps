package com.spaceapps.myapplication.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.LocalDateTime

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "text")
    val text: String,
    @ColumnInfo(name = "created")
    val created: LocalDateTime,
    @ColumnInfo(name = "isLiked")
    val isLiked: Boolean,
    @ColumnInfo(name = "likesCount")
    val likesCount: Long,
    @ColumnInfo(name = "commentsCount")
    val commentsCount: Long
)
