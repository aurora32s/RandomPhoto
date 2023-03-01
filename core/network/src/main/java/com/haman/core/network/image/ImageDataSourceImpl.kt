package com.haman.core.network.image

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
    /**
     * 특정 이미지 요청
     * @param id 이미지 id
     * @param width 이미지 가로 길이
     * @param height 이미지 세로 길이
     */
    override suspend fun getImage(id: String, width: Int, height: Int): Result<ByteArray> {
        return tryCatching(TAG, "getImage") {
            val response = imageApiService.getImage(id, width, height)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.bytes()
            } else throw ImageRequestNetworkException(response.message())
        }
    }

    /**
     * 특정 이미지의 정보 요청
     * @param id 이미지 id
     */
    override suspend fun getImageInfo(id: String): Result<ImageResponse> {
        return tryCatching(TAG, "getImageInfo") {
            val response = imageApiService.getImageInfo(id)
            if (response.isSuccessful && response.body() != null) {
                val image = response.body()
                image ?: throw NoneImageResponseException()
            } else throw ImageRequestNetworkException(response.message())
        }
    }

    /**
     * 랜덤 이미지 정보 요청
     * @param seed 랜덤에 사용될 seed 정보
     */
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