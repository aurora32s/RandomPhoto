package com.haman.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haman.core.model.entity.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKeys>)

    @Query("SELECT * FROM remove_keys WHERE id = :imageId")
    suspend fun remoteKeysImageId(imageId: String): RemoteKeys?

    @Query("DELETE FROM remove_keys")
    suspend fun deleteAll()
}