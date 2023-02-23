package com.haman.core.data.image

import android.graphics.Bitmap
import com.haman.core.data.repository.ImageRepository
import com.haman.core.network.source.ImageDataSource
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageDataSource: ImageDataSource
): ImageRepository {
    override suspend fun getImage(id: String): Result<Bitmap> {
        // 1. 메모리에 캐싱되어 있는지 확인

        // 2. Disk 에 캐싱되어 있는지 확인

        // 3. 1,2 모두 없으면 네트워크에 요청
        val imageBitmap = imageDataSource.getImage(id)
        return imageBitmap
    }
}