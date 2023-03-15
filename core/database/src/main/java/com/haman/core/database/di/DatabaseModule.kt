package com.haman.core.database.di

import android.content.Context
import androidx.room.Room
import com.haman.core.database.RandomPhotoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): RandomPhotoDatabase {
        return Room.databaseBuilder(
            context,
            RandomPhotoDatabase::class.java,
            RandomPhotoDatabase.DATABASE_NAME
        ).build()
    }
}