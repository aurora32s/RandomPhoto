package com.haman.feature.detail

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.haman.core.designsystem.component.AsyncImage
import com.haman.core.model.ui.ImageUiModel

@Composable
fun DetailScreen(
    imageId: String,
    viewModel: DetailViewModel = hiltViewModel()
) {
    LaunchedEffect(null) {
        viewModel.getImageInfo(imageId = imageId)
    }
    val detailUiState = viewModel.detailUiState.collectAsState()
    when (val value = detailUiState.value) {
        DetailUiState.Error -> {}
        DetailUiState.Loading -> {}
        is DetailUiState.Success -> {
            DetailBody(
                image = value.image,
                loadImage = viewModel::getImageByUrl
            )
        }
    }
}

@Composable
fun DetailBody(
    image: ImageUiModel,
    loadImage: suspend (String, Int, Int) -> Bitmap?
) {
    AsyncImage(id = image.id, load = loadImage)
}