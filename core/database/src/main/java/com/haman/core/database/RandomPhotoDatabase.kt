package com.haman.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.haman.core.database.dao.ImageDao
import com.haman.core.database.dao.RemoteKeysDao
import com.haman.core.model.entity.ImageEntity
import com.haman.core.model.entity.RemoteKeys

@Database(
    entities = [
        ImageEntity::class,
        RemoteKeys::class
    ],
    version = RandomPhotoDatabase.DATABASE_VERSION,
    exportSchema = false // auto migration deny
)
abstract class RandomPhotoDatabase : RoomDatabase() {
    abstract fun getImageDao(): ImageDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao

    companion object {
        const val DATABASE_NAME = "Random.db"
        const val DATABASE_VERSION = 1
    }
}