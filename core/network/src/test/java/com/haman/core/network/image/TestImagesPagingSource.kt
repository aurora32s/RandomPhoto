package com.haman.core.network.image

import androidx.paging.PagingSource
import com.haman.core.common.exception.ImageIOException
import com.haman.core.common.exception.NoneImageResponseException
import com.haman.core.model.response.ImageResponse
import com.haman.core.testing.response.MockResponseFileReader
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
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import java.net.HttpURLConnection

@OptIn(ExperimentalCoroutinesApi::class)
class TestImagesPagingSource {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var imagePagingSource: PagingSource<Int, ImageResponse>

    private val firstResponse = (1..2).map {
        ImageResponse(
            id = "$it",
            author = "seomseom",
            width = it * 1000,
            height = it * 1000,
            imageUrl = "image_$it"
        )
    }

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

        imagePagingSource = ImagesPagingSource(imageApiService)
    }

    @After
    fun clear() {
        mockWebServer.shutdown()
    }

    @Test
    fun pagingSource_load_Success() = runTest {
        // 1. Given
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("images_paging_success.json").content)
        mockWebServer.enqueue(response)

        // 2. When
        val loadResult = imagePagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        // 3. Then
        assertThat(
            loadResult,
            `is`(
                PagingSource.LoadResult.Page(
                    data = firstResponse,
                    prevKey = null,
                    nextKey = 2
                )
            )
        )
    }

    @Test
    fun pagingSource_load_Success_lastPage() = runTest {
        // 1. Given
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("images_paging_success.json").content)
        mockWebServer.enqueue(response)

        // 2. When
        val loadResult = imagePagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 5,
                placeholdersEnabled = false
            )
        )

        // 3. Then
        assertThat(
            loadResult,
            `is`(
                PagingSource.LoadResult.Page(
                    data = firstResponse,
                    prevKey = null,
                    nextKey = null
                )
            )
        )
    }


    @Test
    fun pagingSource_Fail_Network_Disconnect() = runTest {
        // 1. Given
        val response = MockResponse()
            .setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
        mockWebServer.enqueue(response)

        // 2. When
        val loadResult = imagePagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        assertThat(
            (loadResult as PagingSource.LoadResult.Error).throwable,
            IsInstanceOf(ImageIOException::class.java)
        )
    }


    @Test
    fun pagingSource_Fail_Response_error() = runTest {
        // 1. Given
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_NO_CONTENT)
        mockWebServer.enqueue(response)

        // 2. When
        val loadResult = imagePagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        assertThat(
            (loadResult as PagingSource.LoadResult.Error).throwable.cause,
            IsInstanceOf(NoneImageResponseException::class.java)
        )
    }
}