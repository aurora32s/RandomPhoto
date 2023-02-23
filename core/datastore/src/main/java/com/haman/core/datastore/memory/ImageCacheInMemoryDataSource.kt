package com.haman.core.datastore.memory

import android.graphics.Bitmap
import android.util.LruCache
import com.haman.core.datastore.source.ImageCacheDataSource
import javax.inject.Inject

class ImageCacheInMemoryDataSource @Inject constructor() : ImageCacheDataSource {
    // Memory Cache
    private val maxMemorySize = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = maxMemorySize / 8
    private val cache = object : LruCache<String, Bitmap>(cacheSize) {
        override fun sizeOf(key: String, value: Bitmap): Int {
            return super.sizeOf(key, value)
        }
    }

    override fun getImage(id: String): Bitmap? {
        return cache.get(id)
    }

    override fun addImage(id: String, bitmap: Bitmap) {
        cache.put(id, bitmap)
    }
}