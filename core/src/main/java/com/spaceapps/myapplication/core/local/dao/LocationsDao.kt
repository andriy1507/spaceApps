package com.spaceapps.myapplication.core.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spaceapps.myapplication.core.models.local.locations.LocationEntity

@Dao
interface LocationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(locations: List<LocationEntity>)

    @Query("SELECT * FROM Locations WHERE name LIKE :name")
    fun pagingSource(name: String?): PagingSource<Int, LocationEntity>

    @Query("DELETE FROM Locations")
    suspend fun clearAll()

    @Query("DELETE FROM Locations WHERE id = :id")
    suspend fun deleteById(id: Int)
}
