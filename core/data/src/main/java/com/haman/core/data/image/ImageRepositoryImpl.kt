package com.haman.core.data.image

import android.graphics.Bitmap
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.haman.core.common.di.IODispatcher
import com.haman.core.data.model.Image
import com.haman.core.data.model.toModel
import com.haman.core.data.repository.ImageRepository
import com.haman.core.datastore.di.Disk
import com.haman.core.datastore.di.Memory
import com.haman.core.datastore.source.ImageCacheDataSource
import com.haman.core.network.image.ImageApiService
import com.haman.core.network.image.ImagesPagingSource
import com.haman.core.network.source.ImageDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @Memory private val imageCachedInMemoryDataSource: ImageCacheDataSource,
    @Disk private val imageCachedInDiskDataSource: ImageCacheDataSource,
    private val imageApiService: ImageApiService,
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

    override fun getImagesInfo(): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(
                pageSize = ImagesPagingSource.LIMIT_PER_PAGE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ImagesPagingSource(imageApiService = imageApiService)
            }
        ).flow
            .map { it.map { image -> image.toModel() } }
    }
}