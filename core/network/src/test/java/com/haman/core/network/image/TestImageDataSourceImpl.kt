package com.haman.core.network.image

import com.haman.core.common.exception.ImageRequestNetworkException
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
import okhttp3.mockwebserver.SocketPolicy
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import java.net.ConnectException
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

    @After
    fun clear() {
        mockWebServer.shutdown()
    }

    @Test
    fun getImageInfo_Success() = runTest {
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

    @Test
    fun getImageInfo_Fail_Response_error() = runTest {
        // 1. Given
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_NO_CONTENT)
        mockWebServer.enqueue(response)

        // 2. When
        val image = imageDataSource.getImageInfo("1")
        val result = image.exceptionOrNull()
        // 3. Then
        assertThat(result, IsInstanceOf(ImageRequestNetworkException::class.java))
    }

    @Test
    fun getImageInfo_Fail_Network_Disconnect() = runTest {
        // 1. Given
        val response = MockResponse()
            .setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
        mockWebServer.enqueue(response)

        // 2. When
        val image = imageDataSource.getImageInfo("1")
        val result = image.exceptionOrNull()

        // 3. Then
        assertThat(result, IsInstanceOf(ConnectException::class.java))
    }
}