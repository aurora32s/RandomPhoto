package com.haman.core.data.image

import android.graphics.Bitmap
import com.haman.core.data.repository.ImageRepository
import com.haman.core.datastore.di.Disk
import com.haman.core.datastore.di.Memory
import com.haman.core.datastore.source.ImageCacheDataSource
import com.haman.core.network.source.ImageDataSource
import java.io.IOException
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    @Memory private val imageCachedInMemoryDataSource: ImageCacheDataSource,
    @Disk private val imageCachedInDiskDataSource: ImageCacheDataSource,
    private val imageDataSource: ImageDataSource
) : ImageRepository {
    override suspend fun getImage(id: String, width: Int, height: Int): Bitmap? {
        return try {
            // 1. 메모리에 캐싱되어 있는지 확인
            val bitmapInMemory = imageCachedInMemoryDataSource.getImage(id)
            if (bitmapInMemory != null) return bitmapInMemory

            // 2. Disk 에 캐싱되어 있는지 확인
            val bitmapInDisk = imageCachedInDiskDataSource.getImage(id)
            if (bitmapInDisk != null) return bitmapInDisk

            // 3. 1,2 모두 없으면 네트워크에 요청
            val bitmapFromApi = imageDataSource.getImage(id, width, height).getOrNull()
            if (bitmapFromApi != null) {
                imageCachedInMemoryDataSource.addImage(id, bitmapFromApi)
                imageCachedInDiskDataSource.addImage(id, bitmapFromApi)
            }
            bitmapFromApi
        } catch (exception: IOException) {
            null
        }
    }
}