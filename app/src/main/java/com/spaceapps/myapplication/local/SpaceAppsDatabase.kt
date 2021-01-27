package com.spaceapps.myapplication.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.spaceapps.myapplication.models.CommentEntity
import com.spaceapps.myapplication.models.PostEntity

@Database(version = 1, entities = [PostEntity::class, CommentEntity::class])
@TypeConverters(RoomTypeConverters::class)
abstract class SpaceAppsDatabase : RoomDatabase() {

    abstract fun getPostsDao(): PostsDao

    abstract fun getCommentsDao(): CommentsDao
}