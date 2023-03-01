package com.haman.core.data.image

import android.graphics.Bitmap
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.haman.core.common.di.ApplicationScope
import com.haman.core.common.di.IODispatcher
import com.haman.core.common.extension.decodeImage
import com.haman.core.common.extension.tryCatching
import com.haman.core.data.repository.ImageRepository
import com.haman.core.datastore.di.DiskCache
import com.haman.core.datastore.di.MemoryCache
import com.haman.core.datastore.source.ImageCacheDataSource
import com.haman.core.model.entity.ImageEntity
import com.haman.core.model.entity.toEntity
import com.haman.core.network.image.ImageApiService
import com.haman.core.network.image.ImagesPagingSource
import com.haman.core.network.source.ImageDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * 이미지의 기준 사이즈
 * 자세한 설명은 PR 참고
 * https://github.com/aurora32s/Daangn_Test/pull/43
 */
private const val BASE_SIZE = 450
private const val TAG = "com.haman.core.data.image.ImageRepositoryImpl"

/**
 * 이미지와 관련된 Repository 동작 관리
 * @param ioDispatcher Dispatchers.IO
 * @param imageCachedInMemoryDataSource 메모리 캐시
 * @param imageCachedInDiskDataSource Disk 캐시
 * @param imageApiService 서버로 이미지를 요청하는 Api(For Paging3)
 * @param imageDataSource 서버로부터 이미지 정보를 요청하는 Api
 */
class ImageRepositoryImpl @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @MemoryCache private val imageCachedInMemoryDataSource: ImageCacheDataSource,
    @DiskCache private val imageCachedInDiskDataSource: ImageCacheDataSource,
    private val imageApiService: ImageApiService,
    private val imageDataSource: ImageDataSource
) : ImageRepository {
    override suspend fun getImage(
        id: String,
        width: Int,
        height: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Result<Bitmap?> =
        withContext(ioDispatcher) {
            tryCatching(tag = TAG, methodName = "getImage") {
                val imageInMemory = imageCachedInMemoryDataSource.getImage(id, reqWidth, reqHeight)
                if (imageInMemory != null) return@tryCatching imageInMemory

                // Sampling 된 Bitmap 반환
                val imageInDisk = imageCachedInDiskDataSource.getImage(id, reqWidth, reqHeight)
                if (imageInDisk != null) {
                    // Sampling 된 상태로 Bitmap 을 Memory 에 Caching
                    externalScope.launch { imageCachedInMemoryDataSource.addImage(id, imageInDisk) }
                    return@tryCatching imageInDisk
                }

                val newWidth = if (width < height) {
                    BASE_SIZE * width / height
                } else BASE_SIZE
                val newHeight = if (width > height) {
                    BASE_SIZE * height / width
                } else BASE_SIZE

                val imageFromApi: ByteArray? =
                    imageDataSource.getImage(id, newWidth, newHeight).getOrNull()
                // 원본 사이즈 Bitmap
                val bitmap = imageFromApi.decodeImage(newWidth, newHeight)
                if (bitmap != null) {
                    externalScope.launch {
                        // Disk 에는 원본 사이즈 Bitmap 저장
                        imageCachedInDiskDataSource.addImage(id, bitmap)
                    }
                }
                // 실질적으로 반환은 Sampling 된 상태로 반환
                return@tryCatching imageFromApi?.decodeImage(reqWidth, reqHeight)
            }
        }

    override fun getImagesInfo(): Flow<PagingData<ImageEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = ImagesPagingSource.LIMIT_PER_PAGE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                ImagesPagingSource(imageApiService = imageApiService)
            }
        ).flow.map {
            it.map { image -> image.toEntity() }
        }

    override suspend fun getImageInfo(id: String): Result<ImageEntity?> =
        withContext(ioDispatcher) {
            tryCatching(TAG, "getImageInfo") {
                imageDataSource.getImageInfo(id).getOrNull()?.toEntity()
            }
        }

    override suspend fun getRandomImageInfo(seed: String): Result<ImageEntity?> =
        withContext(ioDispatcher) {
            tryCatching(TAG, "getRandomImageInfo") {
                imageDataSource.getRandomImageInfo(seed).getOrNull()?.toEntity()
            }
        }
}