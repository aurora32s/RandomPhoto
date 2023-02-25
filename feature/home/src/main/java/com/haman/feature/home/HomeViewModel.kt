package com.haman.feature.home

import androidx.lifecycle.ViewModel
import com.haman.core.domain.GetImagesInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getImagesInfoUseCase: GetImagesInfoUseCase
) : ViewModel() {
}