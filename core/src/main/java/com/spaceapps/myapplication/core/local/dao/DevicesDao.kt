package com.spaceapps.myapplication.core.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spaceapps.myapplication.core.models.local.devices.DeviceEntity

@Dao
interface DevicesDao {

    @Query("SELECT * FROM Devices")
    fun pagingSource(): PagingSource<Int, DeviceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<DeviceEntity>)

    @Query("DELETE FROM Devices")
    suspend fun clearAll()

    @Query("DELETE FROM Devices WHERE id = :id")
    suspend fun deleteById(id: Int)
}
