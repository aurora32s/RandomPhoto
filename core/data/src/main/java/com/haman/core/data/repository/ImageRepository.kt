package com.haman.core.data.repository

import android.graphics.Bitmap

interface ImageRepository {
    /**
     * 특정 이미지 요청
     * @param id 이미지 id
     */
    suspend fun getImage(id: String): Result<Bitmap>
}