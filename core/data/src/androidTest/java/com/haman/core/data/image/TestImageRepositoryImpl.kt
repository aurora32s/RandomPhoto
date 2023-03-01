package com.haman.core.data.image

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haman.core.data.fake.FakeImageApiService
import com.haman.core.data.fake.FakeImageCacheDataSource
import com.haman.core.data.fake.FakeImageDataSource
import com.haman.core.data.repository.ImageRepository
import com.haman.core.datastore.source.ImageCacheDataSource
import com.haman.core.network.source.ImageDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.lessThan
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestImageRepositoryImpl {

    private lateinit var imageCachedInMemoryDataSource: ImageCacheDataSource
    private lateinit var imageCachedInDiskDataSource: ImageCacheDataSource
    private lateinit var imageDataSource: ImageDataSource
    private lateinit var imageRepository: ImageRepository

    @Before
    fun setup() {
        imageCachedInMemoryDataSource = FakeImageCacheDataSource()
        imageCachedInDiskDataSource = FakeImageCacheDataSource()
        imageDataSource = FakeImageDataSource()

        imageRepository = ImageRepositoryImpl(
            externalScope = CoroutineScope(Dispatchers.IO),
            ioDispatcher = Dispatchers.IO,
            imageCachedInMemoryDataSource,
            imageCachedInDiskDataSource,
            FakeImageApiService(),
            imageDataSource
        )
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

}