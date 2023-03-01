package com.haman.core.domain

import android.graphics.Bitmap
import com.haman.core.data.repository.ImageRepository
import javax.inject.Inject

class GetImageUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    /**
     * 이미지 Bitmap 요청
     * @param id 이미지 id
     * @param width 이미지 원본 가로 길이
     * @param height 이미지 원본 세로 길이
     * @param reqWidth 화면에 보여줄 이미지의 가로길이
     */
    suspend operator fun invoke(id: String, width: Int, height: Int, reqWidth: Int): Bitmap? {
        return imageRepository.getImage(id, width, height, reqWidth).getOrNull()
    }
}