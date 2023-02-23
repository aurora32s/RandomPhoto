package com.haman.core.datastore.source

import android.graphics.Bitmap

/**
 * Image In Memory Cache
 */
interface ImageCacheDataSource {
    /**
     * id 에 해당하는 이미지 요청
     */
    fun getImage(id: String): Bitmap?

    /**
     * id에 해당하는 이미지 저장
     */
    fun addImage(id: String, bitmap: Bitmap)
}