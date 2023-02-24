package com.haman.core.datastore.internal.image

import android.graphics.Bitmap
import com.haman.core.datastore.disk.DiskCache
import com.haman.core.datastore.source.ImageCacheDataSource
import javax.inject.Inject

class ImageCacheInDiskDataSource @Inject constructor(
    private val diskCache: DiskCache
) : ImageCacheDataSource {

    override suspend fun getImage(id: String): Bitmap? {
        return diskCache.getBitmapFromDisk(id)
    }

    override suspend fun addImage(id: String, bitmap: Bitmap) {
        diskCache.putBitmapInDisk(id, bitmap)
    }
}