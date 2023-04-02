package com.haman.feature.home

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.haman.core.domain.GetImageUseCase
import com.haman.core.domain.GetImagesInfoUseCase
import com.haman.core.model.ui.ImageUiModel
import com.haman.core.model.ui.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getImagesInfoUseCase: GetImagesInfoUseCase,
    private val getImageUseCase: GetImageUseCase
) : ViewModel() {
    val imagesInfo: Flow<PagingData<ImageUiModel>> =
        getImagesInfoUseCase()
            .map { it.map { image -> image.toUiModel() } }

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

    private val _listType: MutableStateFlow<ListType> = MutableStateFlow(ListType.GRID)
    val listType: StateFlow<ListType> = _listType.asStateFlow()

    fun setListType(listType: ListType) {
        _listType.value = listType
    }
}

/**
 * Home Screen 에 보이는 리스트 타입
 */
enum class ListType(val column: Int) {
    LINEAR(1),
    GRID(3)
}