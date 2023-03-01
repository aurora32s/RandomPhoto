package com.haman.core.data.image

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.map
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haman.core.data.entity.ImageDiffCallback
import com.haman.core.data.entity.ImagesPagingCallback
import com.haman.core.data.fake.FakeImageApiService
import com.haman.core.data.fake.FakeImageDataSource
import com.haman.core.data.repository.ImageRepository
import com.haman.core.datastore.internal.image.ImageCacheInDiskDataSource
import com.haman.core.datastore.memory.image.ImageCacheInMemoryDataSource
import com.haman.core.datastore.source.ImageCacheDataSource
import com.haman.core.model.entity.ImageEntity
import com.haman.core.model.entity.toEntity
import com.haman.core.model.response.ImageResponse
import com.haman.core.network.source.ImageDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.lessThan
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class TestImageRepositoryImpl {
    private lateinit var superJob: Job
    private lateinit var externalScope: TestScope
    private lateinit var dispatcher: TestDispatcher

    private lateinit var imageCachedInMemoryDataSource: ImageCacheDataSource
    private lateinit var imageCachedInDiskDataSource: ImageCacheDataSource
    private lateinit var imageDataSource: ImageDataSource
    private lateinit var imageRepository: ImageRepository

    @Before
    fun setup() {
        superJob = SupervisorJob()
        externalScope = TestScope(superJob)
        dispatcher = StandardTestDispatcher(externalScope.testScheduler)
        Dispatchers.setMain(dispatcher)

        imageCachedInMemoryDataSource = ImageCacheInMemoryDataSource(dispatcher)
        imageCachedInDiskDataSource = ImageCacheInDiskDataSource(
            ApplicationProvider.getApplicationContext(),
            dispatcher
        )

        imageDataSource = FakeImageDataSource()

        imageRepository = ImageRepositoryImpl(
            externalScope,
            dispatcher,
            imageCachedInMemoryDataSource,
            imageCachedInDiskDataSource,
            FakeImageApiService(),
            imageDataSource
        )
    }

    @After
    fun clear() {
        Dispatchers.resetMain()
    }

    @Test
    fun getImage_Success_from_Server() = runTest {
        // 2. When
        val bitmap = imageRepository.getImage(
            id = "0",
            width = 100,
            height = 200,
            reqWidth = 10
        ).getOrNull()

        // 3. Then
        assertThat(bitmap, `is`(notNullValue()))
        bitmap?.let {
            // Sampling 동작 확인
            assertThat(it.width, `is`(lessThan(100)))
            assertThat(it.height, `is`(lessThan(200)))
        }
    }

    @Test
    fun getImage_Success_from_Disk() = runTest {
        // 1. Given: 서버에서 데이터 요청
        val reqWidth = 10
        imageRepository.getImage(id = "0", width = 100, height = 200, reqWidth).getOrNull()

        // 2. Disk 에 캐싱된 정보
        val bitmap = imageCachedInDiskDataSource.getImage("0", reqWidth)

        // 3. Then
        assertThat(bitmap, `is`(notNullValue()))
    }

    @Test
    fun getImage_Success_Max_from_Disk() = externalScope.runTest {
        (0..1000).map {
            launch {
                val i = Random.nextInt(1, 10)
                imageRepository.getImage("$i", width = 500, 300, 100)
            }
        }.joinAll()
    }

    @Test
    fun getImage_Success_from_Memory() = runTest {
        // 1. Given: 서버와 Disk 에서 데이터 요청
        val reqWidth = 10
        val width = 100
        imageRepository.getImage(id = "0", width, height = 200, reqWidth).getOrNull()
        imageRepository.getImage(id = "0", width, height = 200, reqWidth).getOrNull()

        // 2. Disk 에 캐싱된 정보
        val bitmapOfExactWidth = imageCachedInMemoryDataSource.getImage("0", reqWidth)
        val bitmapOfDiffWidth = imageCachedInMemoryDataSource.getImage("0", width)

        // 3. Then
        // 원본과 동일한 이미지
        assertThat(bitmapOfExactWidth, `is`(notNullValue()))
        assertThat(bitmapOfDiffWidth, `is`(nullValue()))
        bitmapOfExactWidth?.let {
            // 메모리 캐싱은 Sampling 된 상태로 저장
            assertThat(it.width, `is`(lessThan(width)))
        }
    }

    @Test
    fun getImageInfo_Success() = runTest {
        // 1. When
        val image = imageRepository.getImageInfo("4").getOrNull()

        // 2. Then
        assertThat(image, `is`(notNullValue()))
        image?.let {
            assertThat(it.id, `is`("4"))
            assertThat(it, IsInstanceOf(ImageEntity::class.java))
        }
    }

    @Test
    fun getImageInfo_Fail_Image_Not_Found() = runTest {
        // 1. When
        val image = imageRepository.getImageInfo("12").getOrNull()
        //2. Then
        assertThat(image, `is`(nullValue()))
    }

    @Test
    fun getImagesInfo_Success_Transform_From_ImageResponse_To_Entity() = externalScope.runTest {
        val images = PagingData.from(
            (0..10).map {
                ImageResponse("$it", "author$it", it, it, "image_$it")
            }
        ).map { it.toEntity() }
        val differ = AsyncPagingDataDiffer(
            diffCallback = ImageDiffCallback(),
            updateCallback = ImagesPagingCallback(),
            workerDispatcher = dispatcher
        )

        differ.submitData(images)

        advanceUntilIdle()
        assertThat(
            (0..10).map { ImageEntity("$it", "author$it", it, it, "image_$it") },
            `is`(differ.snapshot().items)
        )
    }

}