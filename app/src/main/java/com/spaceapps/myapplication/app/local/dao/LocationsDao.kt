package com.spaceapps.myapplication.app.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spaceapps.myapplication.app.models.local.LocationEntity

@Dao
interface LocationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(locations: List<LocationEntity>)

    @Query("SELECT * FROM Locations")
    fun pagingSource(): PagingSource<Int, LocationEntity>

    @Query("DELETE FROM Locations")
    suspend fun clearAll()
}
