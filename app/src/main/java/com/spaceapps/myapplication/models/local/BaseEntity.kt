package com.spaceapps.myapplication.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entity")
data class BaseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int
)
