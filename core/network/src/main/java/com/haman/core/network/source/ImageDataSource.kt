package com.haman.core.network.source

import android.graphics.Bitmap
import com.haman.core.model.response.ImageResponse

interface ImageDataSource {
    /**
     * 특정 이미지 요청
     * @param id 이미지 id
     * @param width 이미지 가로 길이
     * @param height 이미지 세로 길이
     */
    suspend fun getImage(id: String, width: Int, height: Int): Result<Bitmap>

    /**
     * 특정 이미지의 정보 요청
     * @param id 이미지 id
     */
    suspend fun getImageInfo(id: String): Result<ImageResponse>
}