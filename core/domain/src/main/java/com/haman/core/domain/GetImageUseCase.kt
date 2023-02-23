package com.haman.core.domain

import android.graphics.Bitmap
import com.haman.core.data.repository.ImageRepository
import javax.inject.Inject

class GetImageUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(id: String): Result<Bitmap?> {
        return imageRepository.getImage(id)
    }
}