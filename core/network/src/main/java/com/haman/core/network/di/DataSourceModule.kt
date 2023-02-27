package com.haman.core.network.di

import com.haman.core.network.image.ImageDataSourceImpl
import com.haman.core.network.source.ImageDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {
    @Binds
    @Singleton
    fun bindImageDataSource(
        imageDataSourceImpl: ImageDataSourceImpl
    ): ImageDataSource
}