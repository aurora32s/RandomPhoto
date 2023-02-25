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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getImagesInfoUseCase: GetImagesInfoUseCase,
    private val getImageUseCase: GetImageUseCase
) : ViewModel() {

    private val _listType = MutableStateFlow<ListType>(ListType.LINEAR)
    val listType: StateFlow<ListType>
        get() = _listType.asStateFlow()

    private var loadImageJob = SupervisorJob()

    val imagesInfo: Flow<PagingData<ImageUiModel>> =
        getImagesInfoUseCase()
            .map { it.map { image -> image.toUiModel() } }
            .cachedIn(viewModelScope)

    /**
     * 이미지 id 를 이용해
     */
    suspend fun getImageByUrl(id: String, width: Int, height: Int): Bitmap? {
        return viewModelScope.async {
            getImageByUrl(id, width, height)
        }.await()
    }

    fun toggleListType() {
        viewModelScope.launch {
            _listType.emit(
                when (_listType.value) {
                    ListType.LINEAR -> ListType.GRID
                    ListType.GRID -> ListType.LINEAR
                }
            )
        }
    }
}

enum class ListType(val column: Int) {
    LINEAR(1),
    GRID(3)
}