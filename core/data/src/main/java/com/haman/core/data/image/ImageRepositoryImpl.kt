package com.haman.core.data.image

import android.graphics.Bitmap
import com.haman.core.common.di.IODispatcher
import com.haman.core.data.repository.ImageRepository
import com.haman.core.datastore.di.Disk
import com.haman.core.datastore.di.Memory
import com.haman.core.datastore.source.ImageCacheDataSource
import com.haman.core.network.source.ImageDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @Memory private val imageCachedInMemoryDataSource: ImageCacheDataSource,
    @Disk private val imageCachedInDiskDataSource: ImageCacheDataSource,
    private val imageDataSource: ImageDataSource
) : ImageRepository {
    override suspend fun getImage(id: String, width: Int, height: Int): Result<Bitmap?> =
        withContext(ioDispatcher) {
            runCatching {
                // 1. 메모리에 캐싱되어 있는지 확인\
                val imageInMemory = imageCachedInMemoryDataSource.getImage(id)
                if (imageInMemory != null) return@runCatching imageInMemory

                // 2. Disk 에 캐싱되어 있는지 확인
                val imageInDisk = imageCachedInDiskDataSource.getImage(id)
                if (imageInDisk != null) return@runCatching imageInDisk

                // 3. 1,2 모두 없으면 네트워크에 요청
                val bitmapFromApi = imageDataSource.getImage(id, width, height).getOrNull()
                if (bitmapFromApi != null) {
                    // 3.1 메모리와 Disk 에 모두 저장
                    imageCachedInMemoryDataSource.addImage(id, bitmapFromApi)
                    imageCachedInDiskDataSource.addImage(id, bitmapFromApi)
                }
                return@runCatching bitmapFromApi
            }
        }
}