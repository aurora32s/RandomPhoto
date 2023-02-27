package com.haman.feature.detail

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haman.core.domain.GetImageInfoUseCase
import com.haman.core.domain.GetImageUseCase
import com.haman.core.model.ui.ImageUiModel
import com.haman.core.model.ui.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getImageInfoUseCase: GetImageInfoUseCase,
    private val getImageUseCase: GetImageUseCase
) : ViewModel() {

//    private val _detailUiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
//    val detailUiState: StateFlow<DetailUiState>
//        get() = _detailUiState.asStateFlow()

    suspend fun getImageInfo(imageId: String?): String? {
        return imageId?.let {
            viewModelScope.async {
                val image = getImageInfoUseCase(imageId)
                image?.author
//                if (image != null) _detailUiState.emit(DetailUiState.Success(image.toUiModel()))
//                else _detailUiState.emit(DetailUiState.Error)
            }.await()
        } ?: run {
//            _detailUiState.emit(DetailUiState.Error)
            null
        }
    }

    /**
     * 이미지 id 를 이용해
     */
    suspend fun getImageByUrl(id: String, width: Int, height: Int): Bitmap? {
        return viewModelScope
            .async { getImageUseCase(id, width, height) }
            .await()
    }
}

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(
        val image: ImageUiModel
    ) : DetailUiState

    object Error : DetailUiState
}