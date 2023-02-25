package com.haman.core.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.haman.core.data.repository.ImageRepository
import com.haman.core.model.domain.Image
import com.haman.core.model.domain.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 이미지 리스트 요청
 */
class GetImagesInfoUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    operator fun invoke(): Flow<PagingData<Image>> {
        return imageRepository.getImagesInfo()
            .map { it.map { image -> image.toModel() } }
    }
}