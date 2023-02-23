package com.haman.core.network.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.haman.core.common.exception.NetworkException
import com.haman.core.common.exception.NoneImageResponseException
import com.haman.core.network.source.ImageDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageDataSourceImpl @Inject constructor(
    private val imageApiService: ImageApiService
) : ImageDataSource {
    override suspend fun getImage(id: String, width: Int, height: Int): Result<Bitmap> {
        return runCatching {
            val response = imageApiService.getImage(id, width, height)
            if (response.isSuccessful) {
                val stream = response.body()?.byteStream()
                BitmapFactory.decodeStream(stream)
                    ?: throw NoneImageResponseException(response.message())
            } else throw NetworkException(response.message())
        }
    }
}