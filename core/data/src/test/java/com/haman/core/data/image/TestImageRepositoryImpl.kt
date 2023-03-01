package com.haman.core.data.image

import com.haman.core.data.fake.FakeImageCacheDataSource
import com.haman.core.data.fake.FakeImageDataSource
import com.haman.core.data.image.ImageRepositoryImpl
import com.haman.core.data.repository.ImageRepository
import com.haman.core.datastore.internal.image.ImageCacheInDiskDataSource
import com.haman.core.datastore.source.ImageCacheDataSource
import com.haman.core.network.image.ImageApiService
import com.haman.core.network.source.ImageDataSource
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit

class TestImageRepositoryImpl {

    private lateinit var imageCachedInMemoryDataSource: ImageCacheDataSource
    private lateinit var imageCachedInDiskDataSource: ImageCacheDataSource
    private lateinit var imageDataSource: ImageDataSource

    private lateinit var imageRepository: ImageRepository
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val imageApiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(
                Json {
                    coerceInputValues = true
                    ignoreUnknownKeys = true
                }.asConverterFactory("application/json".toMediaType())
            )
            .client(OkHttpClient())
            .build()
            .create(ImageApiService::class.java)

        imageCachedInMemoryDataSource = FakeImageCacheDataSource()
        imageCachedInDiskDataSource = FakeImageCacheDataSource()
        imageDataSource = FakeImageDataSource()

        imageRepository = ImageRepositoryImpl(
            externalScope = CoroutineScope(Dispatchers.IO),
            ioDispatcher = Dispatchers.IO,
            imageCachedInMemoryDataSource,
            imageCachedInDiskDataSource,
            imageApiService,
            imageDataSource
        )
    }

    @After
    fun clear() {
        mockWebServer.shutdown()
    }
}