package com.haman.core.datastore.source

import android.graphics.Bitmap

/**
 * Image In Memory Cache
 */
interface ImageCacheDataSource {
    /**
     * id 에 해당하는 이미지 요청
     */
    suspend fun getImage(id: String, reqWidth: Int, reqHeight: Int): Bitmap?

    /**
     * id에 해당하는 이미지 저장
     */
    suspend fun addImage(id: String, bitmap: Bitmap)
}