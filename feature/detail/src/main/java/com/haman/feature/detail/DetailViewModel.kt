package com.haman.feature.detail

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haman.core.domain.GetImageInfoUseCase
import com.haman.core.domain.GetImageUseCase
import com.haman.core.model.ui.ImageUiModel
import com.haman.core.model.ui.toUiModel
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
    suspend fun getImageInfo(imageId: String?): ImageUiModel? {
        return imageId?.let {
            viewModelScope.async { getImageInfoUseCase(imageId)?.toUiModel() }.await()
        } ?: run {
            null
        }
    }

    /**
     * 이미지 id 를 이용해 이미지 Bitmap 요청
     * @param imageId 이미지 Id
     * @param width 이미지 원본 가로길이
     * @param height 이미지 원본 세로 길이
     * @param reqWidth 화면에 보여줄 아마자의 가로 길이
     */
    suspend fun getImageByUrl(imageId: String, width: Int, height: Int, reqWidth: Int): Bitmap? {
        return getImageUseCase(imageId, width, height, reqWidth)
    }
}