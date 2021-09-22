package com.spaceapps.myapplication.app.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spaceapps.myapplication.app.models.local.LocationRemoteKey

@Dao
interface LocationsRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<LocationRemoteKey>)

    @Query("SELECT * FROM LocationRemoteKeys WHERE id= :id")
    suspend fun remoteKeysById(id: Int): LocationRemoteKey?

    @Query("DELETE FROM LocationRemoteKeys")
    suspend fun clearAll()
}
