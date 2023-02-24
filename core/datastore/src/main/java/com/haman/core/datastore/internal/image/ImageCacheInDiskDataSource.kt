package com.haman.core.datastore.internal.image

import android.content.Context
import android.graphics.Bitmap
import com.haman.core.datastore.disk.impl.DiskLruCache
import com.haman.core.datastore.source.ImageCacheDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ImageCacheInDiskDataSource @Inject constructor(
    @ApplicationContext
    context: Context
) : ImageCacheDataSource {

    private val diskLruCache = DiskLruCache.open(context.filesDir, 1024 * 1024 * 10)

    override suspend fun getImage(id: String): Bitmap? {
        return diskLruCache.getBitmapFromDisk(id)
    }

    override suspend fun addImage(id: String, bitmap: Bitmap) {
        diskLruCache.putBitmapInDisk(id, bitmap)
    }
}