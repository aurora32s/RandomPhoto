package com.haman.core.domain

import com.haman.core.data.repository.ImageRepository
import com.haman.core.model.domain.Image
import com.haman.core.model.domain.toModel
import javax.inject.Inject

/**
 * 특정 이미지의 정보를 요청하는 Use Case
 */
class GetImageInfoUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(id: String): Image? {
        val image = imageRepository.getImageInfo(id)
        return if (image.isSuccess) image.getOrNull()?.toModel() else null
    }
}