package com.haman.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haman.core.domain.GetImageInfoUseCase
import com.haman.core.model.ui.ImageUiModel
import com.haman.core.model.ui.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getImageInfoUseCase: GetImageInfoUseCase
) : ViewModel() {

    private val _detailUiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailUiState: StateFlow<DetailUiState>
        get() = _detailUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val imageId: String? = savedStateHandle[""]
            imageId?.let {
                val image = getImageInfoUseCase(it)
                if (image != null) _detailUiState.emit(DetailUiState.Success(image.toUiModel()))
                else _detailUiState.emit(DetailUiState.Error)
            }
        }
    }
}

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(
        val image: ImageUiModel
    ) : DetailUiState

    object Error : DetailUiState
}