package com.haman.feature.home

import android.app.Application
import android.content.pm.ApplicationInfo
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.haman.core.domain.GetImageUseCase
import com.haman.core.domain.GetImagesInfoUseCase
import com.haman.core.domain.GetRandomImageInfoUseCase
import com.haman.core.model.ui.ImageUiModel
import com.haman.core.model.ui.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getImagesInfoUseCase: GetImagesInfoUseCase,
    private val getImageUseCase: GetImageUseCase,
    private val getRandomImageInfoUseCase: GetRandomImageInfoUseCase
) : ViewModel() {

    private var loadImageJob = SupervisorJob()
    val imagesInfo: Flow<PagingData<ImageUiModel>> =
        getImagesInfoUseCase()
            .map { it.map { image -> image.toUiModel() } }
            .cachedIn(viewModelScope)

    /**
     * 이미지 id 를 이용해
     */
    suspend fun getImageByUrl(id: String, width: Int, height: Int): Bitmap? {
        return getImageUseCase(id, width, height)
    }
}

enum class ListType(val column: Int) {
    LINEAR(1),
    GRID(3)
}