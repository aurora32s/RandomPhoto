package com.haman.core.datastore.di

import com.haman.core.datastore.internal.image.ImageCacheInDiskDataSource
import com.haman.core.datastore.memory.image.ImageCacheInMemoryDataSource
import com.haman.core.datastore.source.ImageCacheDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class Disk

@Qualifier
annotation class Memory

@Module
@InstallIn(SingletonComponent::class)
interface DatasourceModule {
    @Singleton
    @Memory
    @Binds
    fun bindImageCacheInMemory(
        imageCacheInMemoryDataSource: ImageCacheInMemoryDataSource
    ): ImageCacheDataSource

    @Singleton
    @Disk
    @Binds
    fun bindImageCacheInDisk(
        imageCacheInDiskDataSource: ImageCacheInDiskDataSource
    ): ImageCacheDataSource
}