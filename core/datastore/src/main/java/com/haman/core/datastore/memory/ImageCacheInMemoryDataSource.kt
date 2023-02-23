package com.haman.core.datastore.memory

import android.graphics.Bitmap
import com.haman.core.datastore.source.ImageCacheDataSource
import com.haman.core.datastore.model.Image
import javax.inject.Inject

class ImageCacheInMemoryDataSource @Inject constructor() : ImageCacheDataSource {
    // Memory Cache
    private val cache = hashMapOf<String, Image>()

    override fun getImage(id: String): Bitmap? {
        return cache[id]?.bitmap
    }

    override fun addImage(id: String, bitmap: Bitmap): Pair<String, Bitmap>? {
        var lru: Image? = null
        cache[id] = cache[id]?.copy(storedTime = System.currentTimeMillis())
            ?: run {
                lru = cache.values.minByOrNull { it.storedTime }
                Image(id, bitmap, storedTime = System.currentTimeMillis())
            }
        return lru?.let {
            cache.remove(it.id)
            it.id to it.bitmap
        }
    }

    companion object {
        // 최대로 memory cache 에 저장할 수 있는 이미지 개수
        const val DEFAULT_CACHE_SIZE = 5
    }
}