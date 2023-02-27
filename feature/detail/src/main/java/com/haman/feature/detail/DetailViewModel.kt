package com.haman.feature.detail

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haman.core.domain.GetImageInfoUseCase
import com.haman.core.domain.GetImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getImageInfoUseCase: GetImageInfoUseCase,
    private val getImageUseCase: GetImageUseCase
) : ViewModel() {
    /**
     * 이미지 상세 정보 요청
     * @param imageId 요청할 이미지의 Id
     */
    suspend fun getImageInfo(imageId: String?): String? {
        return imageId?.let {
            viewModelScope.async {
                val image = getImageInfoUseCase(imageId)
                image?.author
            }.await()
        } ?: run {
            null
        }
    }

    /**
     * 이미지 id 를 이용해 이미지 Bitmap 요청
     * @param imageId 이미지 Id
     */
    suspend fun getImageByUrl(imageId: String): Bitmap? {
        return getImageUseCase(imageId)
    }
}