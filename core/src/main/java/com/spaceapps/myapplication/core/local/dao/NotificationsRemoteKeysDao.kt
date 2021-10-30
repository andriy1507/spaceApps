package com.spaceapps.myapplication.core.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spaceapps.myapplication.core.models.local.NotificationRemoteKey

@Dao
interface NotificationsRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<NotificationRemoteKey>)

    @Query("SELECT * FROM NotificationRemoteKeys WHERE id=:id")
    suspend fun remoteKeysById(id: Int): NotificationRemoteKey?

    @Query("DELETE FROM NotificationRemoteKeys")
    suspend fun clearAll()
}
