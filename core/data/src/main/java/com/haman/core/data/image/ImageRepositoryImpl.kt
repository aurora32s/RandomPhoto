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
import com.haman.core.model.entity.ImageData
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
 */
private const val BASE_SIZE = 500
private const val TAG = "com.haman.core.data.image.ImageRepositoryImpl"

/**
 * 이미지와 관련된 Repository 동작 관리
 * @param externalScope 외부 Scope
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

    /**
     * 특정 이미지 요청
     * @param id 이미지 id
     */
    override suspend fun getImage(
        id: String,
        width: Int,
        height: Int,
        reqWidth: Int
    ): Result<Bitmap?> = withContext(ioDispatcher) {
        tryCatching(tag = TAG, methodName = "getImage") {
            // 1. 메모리 캐싱 확인
            val imageInMemory = imageCachedInMemoryDataSource.getImage(id, reqWidth)
            if (imageInMemory != null) return@tryCatching imageInMemory

            // 2. Disk 캐싱 확인
            val imageInDisk = imageCachedInDiskDataSource.getImage(id, reqWidth)
            if (imageInDisk != null) {
                // Sampling 된 상태로 Bitmap 을 Memory 에 캐싱
                externalScope.launch {
                    imageCachedInMemoryDataSource.addImage(
                        id,
                        reqWidth,
                        imageInDisk
                    )
                }
                return@tryCatching imageInDisk
            }

            val (newWidth, newHeight) = getNewSize(width, height)

            // 3. 서버에 원본 이미지 요청
            val imageFromApi: ByteArray? =
                imageDataSource.getImage(id, newWidth, newHeight).getOrNull()
            // 원본 사이즈 Bitmap
            val bitmap = imageFromApi.decodeImage(newWidth)
            if (bitmap != null) {
                externalScope.launch {
                    // Disk 에는 원본 사이즈 Bitmap 저장
                    imageCachedInDiskDataSource.addImage(id, newWidth, bitmap)
                }
            }
            // 실질적으로 반환은 Sampling 된 상태로 반환
            return@tryCatching imageFromApi?.decodeImage(reqWidth)
        }
    }

    /**
     * width : height 과 동일한 비율의 보다 작은 가로/세로 길이 계산
     */
    private fun getNewSize(width: Int, height: Int): Pair<Int, Int> {
        val newWidth = if (width < height) {
            BASE_SIZE * width / height
        } else BASE_SIZE
        val newHeight = if (width > height) {
            BASE_SIZE * height / width
        } else BASE_SIZE

        return newWidth to newHeight
    }

    /**
     * 이미지 리스트 요청
     */
    override fun getImagesInfo(): Flow<PagingData<ImageData>> =
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

    /**
     * 특정 이미지 정보 요청
     * @param id 이미지 id
     */
    override suspend fun getImageInfo(id: String): Result<ImageData?> =
        withContext(ioDispatcher) {
            tryCatching(TAG, "getImageInfo") {
                imageDataSource.getImageInfo(id).getOrNull()?.toEntity()
            }
        }

    /**
     * 랜덤 이미지 정보 요청
     * @param seed 랜덤에 사용될 seed 정보
     */
    override suspend fun getRandomImageInfo(seed: String): Result<ImageData?> =
        withContext(ioDispatcher) {
            tryCatching(TAG, "getRandomImageInfo") {
                imageDataSource.getRandomImageInfo(seed).getOrNull()?.toEntity()
            }
        }
}