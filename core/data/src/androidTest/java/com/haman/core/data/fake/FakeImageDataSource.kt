package com.haman.core.data.fake

import android.graphics.Bitmap
import com.haman.core.common.exception.ImageRequestNetworkException
import com.haman.core.model.response.ImageResponse
import com.haman.core.network.source.ImageDataSource
import java.io.ByteArrayOutputStream
import kotlin.random.Random

class FakeImageDataSource : ImageDataSource {
    override suspend fun getImage(id: String, width: Int, height: Int): Result<ByteArray?> {
        val image = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return image?.let { Result.success(stream.toByteArray()) }
            ?: throw ImageRequestNetworkException()
    }

    override suspend fun getImageInfo(id: String): Result<ImageResponse> {
        val image = images.find { it.id == id }
        return image?.let { Result.success(it) }
            ?: throw ImageRequestNetworkException()
    }

    override suspend fun getRandomImageInfo(seed: String): Result<ImageResponse> {
        val imageId = Random.nextInt(1, 10)
        return Result.success(images[imageId])
    }
}

private val images = (1..10).map {
    ImageResponse(
        id = "$it",
        author = "author$it",
        width = it * 1000,
        height = it * 1000,
        imageUrl = "image_$it"
    )
}