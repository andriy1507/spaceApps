package com.spaceapps.myapplication.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spaceapps.myapplication.models.PostEntity

@Dao
interface PostsDao {

    @Query("SELECT * FROM PostEntity")
    fun getAllPosts(): LiveData<List<PostEntity>>

    @Query("SELECT * FROM PostEntity")
    fun getPostsDataSource(): PagingSource<Int, PostEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(entity: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAll(vararg entity: PostEntity)

    @Query("DELETE FROM PostEntity")
    fun clearAll()
}
