package com.haman.core.domain

import com.haman.core.data.repository.ImageRepository
import com.haman.core.model.domain.Image
import com.haman.core.model.domain.toModel
import javax.inject.Inject

class GetRandomImageInfoUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(seed: String): Image? {
        val image = imageRepository.getRandomImageInfo(seed)
        return if (image.isSuccess) image.getOrNull()?.toModel() else null
    }
}