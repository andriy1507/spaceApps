package com.spaceapps.myapplication.core.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spaceapps.myapplication.core.models.local.notifications.NotificationEntity

@Dao
interface NotificationsDao {

    @Query("SELECT * FROM Notifications")
    fun pagingSource(): PagingSource<Int, NotificationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<NotificationEntity>)

    @Query("DELETE FROM Notifications")
    suspend fun clearAll()

    @Query("DELETE FROM Notifications WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("UPDATE Notifications SET is_viewed = 1 WHERE id = :id")
    suspend fun updateViewedById(id: Int)
}
