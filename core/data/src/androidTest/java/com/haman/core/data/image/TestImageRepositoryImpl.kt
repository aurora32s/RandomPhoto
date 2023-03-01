package com.haman.core.data.image

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haman.core.data.fake.FakeImageApiService
import com.haman.core.data.fake.FakeImageDataSource
import com.haman.core.data.repository.ImageRepository
import com.haman.core.datastore.internal.image.ImageCacheInDiskDataSource
import com.haman.core.datastore.memory.image.ImageCacheInMemoryDataSource
import com.haman.core.datastore.source.ImageCacheDataSource
import com.haman.core.network.source.ImageDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.lessThan
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

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
    fun getImageInfo_Success() {
        // 1. 
    }

}