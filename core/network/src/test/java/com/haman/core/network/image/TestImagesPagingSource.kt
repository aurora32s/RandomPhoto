package com.haman.core.network.image

import androidx.paging.PagingSource
import com.haman.core.model.response.ImageResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit

class TestImagesPagingSource {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var imagePagingSource: PagingSource<Int, ImageResponse>

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
                }.asConverterFactory()
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
}