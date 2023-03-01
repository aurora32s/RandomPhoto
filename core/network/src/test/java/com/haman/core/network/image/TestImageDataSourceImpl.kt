package com.haman.core.network.image

import com.haman.core.network.source.ImageDataSource
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit

/**
 * ImageDataSourceImpl 테스트 코드
 */
class TestImageDataSourceImpl {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var imageDataSource: ImageDataSource

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val imageApiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(
                Json {}.asConverterFactory("application/json".toMediaType())
            )
            .client(OkHttpClient())
            .build()
            .create(ImageApiService::class.java)

        imageDataSource = ImageDataSourceImpl(imageApiService)
    }

    @After
    fun clear() {
        mockWebServer.shutdown()
    }
}