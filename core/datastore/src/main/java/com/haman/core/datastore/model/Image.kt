package com.haman.core.datastore.model

import android.graphics.Bitmap

/**
 * 캐싱에 사용될 Image 정보
 */
internal data class Image(
    val id: String, // 이미지 Id
    val bitmap: Bitmap, // 이미지 Bitmap
    val storedTime: Long // 저장된 시간
)
