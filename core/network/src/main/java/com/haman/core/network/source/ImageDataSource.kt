package com.haman.core.network.source

import android.graphics.Bitmap

interface ImageDataSource {
    /**
     * 특정 이미지 요청
     * @param id 이미지 id
     */
    suspend fun getImage(id: String): Result<Bitmap>
}