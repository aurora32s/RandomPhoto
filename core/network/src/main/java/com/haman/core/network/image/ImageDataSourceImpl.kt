package com.haman.core.network.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.haman.core.common.exception.ImageRequestNetworkException
import com.haman.core.common.exception.NoneImageResponseException
import com.haman.core.common.extension.tryCatching
import com.haman.core.model.response.ImageResponse
import com.haman.core.network.source.ImageDataSource
import javax.inject.Inject

private const val TAG = "com.haman.core.network.image.ImageDataSourceImpl"

class ImageDataSourceImpl @Inject constructor(
    private val imageApiService: ImageApiService
) : ImageDataSource {
    override suspend fun getImage(id: String, width: Int, height: Int): Result<Bitmap> {
        return tryCatching(TAG, "getImage") {
            val response = imageApiService.getImage(id, width, height)
            if (response.isSuccessful && response.body() != null) {
                val stream = response.body()?.byteStream()
                BitmapFactory.decodeStream(stream)
                    ?: throw NoneImageResponseException(response.message())
            } else throw ImageRequestNetworkException(response.message())
        }
    }

    override suspend fun getImageInfo(id: String): Result<ImageResponse> {
        return tryCatching(TAG, "getImageInfo") {
            val response = imageApiService.getImageInfo(id)
            if (response.isSuccessful && response.body() != null) {
                val image = response.body()
                image ?: throw NoneImageResponseException()
            } else throw ImageRequestNetworkException(response.message())
        }
    }

    override suspend fun getRandomImageInfo(seed: String): Result<ImageResponse> {
        return tryCatching(TAG, "getRandomImageInfo") {
            val response = imageApiService.getRandomImageInfo(seed)
            if (response.isSuccessful && response.body() != null) {
                val image = response.body()
                image ?: throw NoneImageResponseException()
            } else throw ImageRequestNetworkException(response.message())
        }
    }
}