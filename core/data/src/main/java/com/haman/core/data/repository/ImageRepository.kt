package com.haman.core.data.repository

import android.graphics.Bitmap
import androidx.paging.PagingData
import com.haman.core.data.model.ImageEntity
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    /**
     * 특정 이미지 요청
     * @param id 이미지 id
     */
    suspend fun getImage(id: String, width: Int, height: Int): Result<Bitmap?>

    /**
     * 이미지 리스트 요청
     */
    fun getImagesInfo(): Flow<PagingData<ImageEntity>>
}