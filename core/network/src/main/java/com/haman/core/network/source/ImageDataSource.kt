package com.haman.core.network.source

import com.haman.core.model.response.ImageResponse

interface ImageDataSource {
    /**
     * 특정 이미지 요청
     * @param id 이미지 id
     * @param width 이미지 가로 길이
     * @param height 이미지 세로 길이
     */
    suspend fun getImage(id: String, width: Int, height: Int): Result<ByteArray?>

    /**
     * 특정 이미지의 정보 요청
     * @param id 이미지 id
     */
    suspend fun getImageInfo(id: String): Result<ImageResponse>

    /**
     * 랜덤 이미지 정보 요청
     * @param seed 랜덤에 사용될 seed 정보
     */
    suspend fun getRandomImageInfo(seed: String): Result<ImageResponse>

    /**
     * 이미지 정보 리스트 요청
     */
    suspend fun getImagesInfo(page: Int, limit: Int): Result<List<ImageResponse>>
}