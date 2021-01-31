package com.spaceapps.myapplication.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.spaceapps.myapplication.models.CommentEntity

@Dao
interface CommentsDao {

    @Query("SELECT * FROM CommentEntity WHERE postId == :postId")
    fun getCommentsByPostId(postId: Long): LiveData<List<CommentEntity>>

    @Insert
    fun saveAll(vararg comments: CommentEntity)
}
