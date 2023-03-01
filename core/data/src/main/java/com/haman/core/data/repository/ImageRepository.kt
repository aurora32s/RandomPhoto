package com.haman.core.data.repository

import android.graphics.Bitmap
import androidx.paging.PagingData
import com.haman.core.model.entity.ImageEntity
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    /**
     * 특정 이미지 요청
     * @param id 이미지 id
     */
    suspend fun getImage(
        id: String,
        width: Int,
        height: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Result<Bitmap?>

    /**
     * 이미지 리스트 요청
     */
    fun getImagesInfo(): Flow<PagingData<ImageEntity>>

    /**
     * 특정 이미지 정보 요청
     * @param id 이미지 id
     */
    suspend fun getImageInfo(id: String): Result<ImageEntity?>

    /**
     * 랜덤 이미지 정보 요청
     * @param seed 랜덤에 사용될 seed 정보
     */
    suspend fun getRandomImageInfo(seed: String): Result<ImageEntity?>
}