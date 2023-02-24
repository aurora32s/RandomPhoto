package com.haman.core.datastore.di

import com.haman.core.datastore.disk.DiskCache
import com.haman.core.datastore.disk.impl.DiskLruCache
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CacheModule {
}