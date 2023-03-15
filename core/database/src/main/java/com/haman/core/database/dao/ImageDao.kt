package com.haman.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haman.core.model.entity.ImageEntity

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(image: List<ImageEntity>)

    @Query("SELECT * FROM image")
    fun images(): PagingSource<Int, ImageEntity>

    @Query("DELETE From image")
    suspend fun deleteAll()
}