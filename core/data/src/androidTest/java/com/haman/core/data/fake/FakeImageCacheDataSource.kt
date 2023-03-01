package com.haman.core.data.fake

import android.graphics.Bitmap
import com.haman.core.datastore.source.ImageCacheDataSource

/**
 * TestImageRepositoryImpl 에서 사용할 Fake DataSource
 */
class FakeImageCacheDataSource : ImageCacheDataSource {

    val cache = hashMapOf<String, Bitmap>()

    override suspend fun getImage(id: String, reqWidth: Int): Bitmap? {
        return cache["${id}_$reqWidth"]
    }

    override suspend fun addImage(id: String, width: Int, bitmap: Bitmap) {
        cache["${id}_$width"] = bitmap
    }
}