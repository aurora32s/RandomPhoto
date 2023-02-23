package com.haman.core.datastore.internal

import android.graphics.Bitmap
import com.haman.core.datastore.source.ImageCacheDataSource
import javax.inject.Inject

class ImageCacheInDiskDataSource @Inject constructor() : ImageCacheDataSource {
    override fun getImage(id: String): Bitmap? {
        TODO("Not yet implemented")
    }

    override fun addImage(id: String, bitmap: Bitmap) {
        TODO("Not yet implemented")
    }
}