package com.haman.core.data.image

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haman.core.data.fake.FakeImageApiService
import com.haman.core.data.fake.FakeImageCacheDataSource
import com.haman.core.data.fake.FakeImageDataSource
import com.haman.core.data.repository.ImageRepository
import com.haman.core.datastore.source.ImageCacheDataSource
import com.haman.core.network.source.ImageDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.lessThan
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class TestImageRepositoryImpl {

    private lateinit var imageCachedInMemoryDataSource: ImageCacheDataSource
    private lateinit var imageCachedInDiskDataSource: ImageCacheDataSource
    private lateinit var imageDataSource: ImageDataSource
    private lateinit var imageRepository: ImageRepository

    private val superJob = SupervisorJob()
    private val externalScope = TestScope(superJob)
    private val dispatcher = StandardTestDispatcher(externalScope.testScheduler)

    @Before
    fun setup() {
        imageCachedInMemoryDataSource = FakeImageCacheDataSource()
        imageCachedInDiskDataSource = FakeImageCacheDataSource()
        imageDataSource = FakeImageDataSource()
        Dispatchers.setMain(dispatcher)

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

}