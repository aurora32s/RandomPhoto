package com.haman.core.designsystem.util

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import com.haman.core.model.ui.ImageUiModel

/**
 * 다양한 타입으로 들어올 수 있는 이미지 리소스들을 관리
 */
sealed interface ImageType {
    data class DrawableImage(
        @DrawableRes
        val id: Int
    ) : ImageType

    data class AsyncImage(
        val image: ImageUiModel,
        val load: suspend (String, Int, Int) -> Bitmap?
    ) : ImageType
}