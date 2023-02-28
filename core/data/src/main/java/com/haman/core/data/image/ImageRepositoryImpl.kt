package com.haman.core.data.image

import android.graphics.Bitmap
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.haman.core.common.di.ApplicationScope
import com.haman.core.common.di.IODispatcher
import com.haman.core.common.extension.tryCatching
import com.haman.core.data.repository.ImageRepository
import com.haman.core.datastore.di.Disk
import com.haman.core.datastore.di.Memory
import com.haman.core.datastore.source.ImageCacheDataSource
import com.haman.core.model.entity.ImageEntity
import com.haman.core.model.entity.toEntity
import com.haman.core.network.image.ImageApiService
import com.haman.core.network.image.ImagesPagingSource
import com.haman.core.network.source.ImageDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * 이미지의 기준 사이즈
 * 자세한 설명은 PR 참고
 * https://github.com/aurora32s/Daangn_Test/pull/43
 */
private const val BASE_SIZE = 10
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
    @Memory private val imageCachedInMemoryDataSource: ImageCacheDataSource,
    @Disk private val imageCachedInDiskDataSource: ImageCacheDataSource,
    private val imageApiService: ImageApiService,
    private val imageDataSource: ImageDataSource
) : ImageRepository {
    override suspend fun getImage(id: String, width: Int, height: Int): Result<Bitmap?> =
        withContext(ioDispatcher) {
            tryCatching(tag = TAG, methodName = "getImage") {
                // 1. 메모리에 캐싱되어 있는지 확인\
                val imageInMemory = imageCachedInMemoryDataSource.getImage(id)
                if (imageInMemory != null) return@tryCatching imageInMemory

                // 2. Disk 에 캐싱되어 있는지 확인
                val imageInDisk = imageCachedInDiskDataSource.getImage(id)
                if (imageInDisk != null) {
                    launch { imageCachedInMemoryDataSource.addImage(id, imageInDisk) }
                    return@tryCatching imageInDisk
                }

                // 3. 1,2 모두 없으면 네트워크에 요청
                // 3.1 먼저 이미지의 가로와 세로 길이를 알기 위해 서버에 이미지 정보 요청
//                val imageInfo = imageDataSource.getImageInfo(id).getOrNull()
//                imageInfo ?: return@tryCatching null
//
//                val newWidth = if (imageInfo.width < imageInfo.height) {
//                    BASE_SIZE * imageInfo.width / imageInfo.height
//                } else BASE_SIZE
//                val newHeight = if (imageInfo.width > imageInfo.height) {
//                    BASE_SIZE * imageInfo.height / imageInfo.width
//                } else BASE_SIZE

                val newWidth = if (width < height) {
                    BASE_SIZE * width / height
                } else BASE_SIZE
                val newHeight = if (width > height) {
                    BASE_SIZE * height / width
                } else BASE_SIZE

                // 3.2 받아온 이미지 정보를 바탕으로 실제 이미지 요청
                val bitmapFromApi =
                    imageDataSource.getImage(id, newWidth, newHeight).getOrNull()
//                if (bitmapFromApi != null) {
//                    // 3.3 메모리와 Disk 에 모두 저장
//                    launch { imageCachedInDiskDataSource.addImage(id, bitmapFromApi) }
//                }
                return@tryCatching bitmapFromApi
            }
        }

    override fun getImagesInfo(): Flow<PagingData<ImageEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = ImagesPagingSource.LIMIT_PER_PAGE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ImagesPagingSource(imageApiService = imageApiService)
            }
        ).flow.map {
            externalScope.coroutineContext.cancel()
            it.map { image ->
                externalScope.launch(ioDispatcher) {
                    getImage(
                        image.id,
                        image.width,
                        image.height
                    )
                }
                image.toEntity()
            }
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