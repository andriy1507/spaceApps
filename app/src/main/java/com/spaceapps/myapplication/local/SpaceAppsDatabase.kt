package com.spaceapps.myapplication.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.spaceapps.myapplication.models.local.BaseEntity

@Database(version = 1, entities = [BaseEntity::class], exportSchema = false)
@TypeConverters(RoomTypeConverters::class)
abstract class SpaceAppsDatabase : RoomDatabase()
