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
annotation class MemoryCache
@Qualifier
annotation class DiskCache

@Module
@InstallIn(SingletonComponent::class)
interface DatasourceModule {
    @Binds
    @Singleton
    @MemoryCache
    fun bindImageCacheInMemory(
        imageCacheInMemoryDataSource: ImageCacheInMemoryDataSource
    ): ImageCacheDataSource

    @Binds
    @Singleton
    @DiskCache
    fun bindImageCacheInDisk(
        imageCacheInDiskDataSource: ImageCacheInDiskDataSource
    ): ImageCacheDataSource
}