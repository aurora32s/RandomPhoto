package com.haman.core.network.image

import com.haman.core.network.response.MockResponseFileReader
import com.haman.core.network.source.ImageDataSource
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import java.net.HttpURLConnection

/**
 * ImageDataSourceImpl 테스트 코드
 */
@OptIn(ExperimentalCoroutinesApi::class)
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
                Json {
                    coerceInputValues = true
                    ignoreUnknownKeys = true
                }.asConverterFactory("application/json".toMediaType())
            )
            .client(OkHttpClient())
            .build()
            .create(ImageApiService::class.java)

        imageDataSource = ImageDataSourceImpl(imageApiService)
    }

    @Test
    fun `특정_이미지_정보_요청에_성공`() = runTest {
        // 1. Given
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("image_info_success.json").content)
        mockWebServer.enqueue(response)

        // 2. When
        val image = imageDataSource.getImageInfo("1").getOrNull()

        // 3. Them
        assertThat(image, `is`(notNullValue()))
    }

    @After
    fun clear() {
        mockWebServer.shutdown()
    }
}