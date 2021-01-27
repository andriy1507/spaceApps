package com.spaceapps.myapplication.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CommentEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    @ColumnInfo(name = "text")
    val text: String,
    @ColumnInfo(name = "postId")
    val postId: Long,
    @ColumnInfo(name = "userId")
    val userId: Long
)