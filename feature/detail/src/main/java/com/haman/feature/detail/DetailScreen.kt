package com.haman.feature.detail

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.haman.core.designsystem.component.AsyncImage

@Composable
fun DetailScreen(
    imageId: String,
    viewModel: DetailViewModel = hiltViewModel()
) {
    AsyncImage(id = imageId, load = viewModel::getImageByUrl)
}