package com.haman.core.data.image

import android.graphics.Bitmap
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.haman.core.common.di.IODispatcher
import com.haman.core.data.repository.ImageRepository
import com.haman.core.datastore.di.Disk
import com.haman.core.datastore.di.Memory
import com.haman.core.datastore.source.ImageCacheDataSource
import com.haman.core.model.entity.ImageEntity
import com.haman.core.model.entity.toEntity
import com.haman.core.network.image.ImageApiService
import com.haman.core.network.image.ImagesPagingSource
import com.haman.core.network.source.ImageDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.max

private const val BASE_SIZE = 500

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
                // 3.1 먼저 이미지의 가로와 세로 길이를 알기 위해 서버에 이미지 정보 요청
                val imageInfo = imageDataSource.getImageInfo(id).getOrNull()
                imageInfo ?: return@runCatching null

                val newWidth = if (imageInfo.width < imageInfo.height) {
                    BASE_SIZE * imageInfo.width / imageInfo.height
                } else BASE_SIZE
                val newHeight = if (imageInfo.width > imageInfo.height) {
                    BASE_SIZE * imageInfo.height / imageInfo.width
                } else BASE_SIZE

                // 3.2 받아온 이미지 정보를 바탕으로 실제 이미지 요청
                val bitmapFromApi =
                    imageDataSource.getImage(id, newWidth, newHeight).getOrNull()
                if (bitmapFromApi != null) {
                    // 3.1 메모리와 Disk 에 모두 저장
                    launch { imageCachedInMemoryDataSource.addImage(id, bitmapFromApi) }
                    launch { imageCachedInDiskDataSource.addImage(id, bitmapFromApi) }
                }
                return@runCatching bitmapFromApi
            }
        }

    override fun getImagesInfo(): Flow<PagingData<ImageEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = ImagesPagingSource.LIMIT_PER_PAGE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ImagesPagingSource(imageApiService = imageApiService)
            }
        ).flow
            .map { it.map { image -> image.toEntity() } }
    }

    override suspend fun getImageInfo(id: String): Result<ImageEntity?> =
        withContext(ioDispatcher) {
            runCatching {
                return@runCatching imageDataSource.getImageInfo(id)
                    .getOrNull()?.toEntity()
            }
        }

    override suspend fun getRandomImageInfo(seed: String): Result<ImageEntity?> =
        withContext(ioDispatcher) {
            runCatching {
                return@runCatching imageDataSource.getRandomImageInfo(seed)
                    .getOrNull()?.toEntity()
            }
        }
}