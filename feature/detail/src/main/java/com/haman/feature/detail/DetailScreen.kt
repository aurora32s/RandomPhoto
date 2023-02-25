package com.haman.feature.detail

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.haman.core.designsystem.component.AsyncImage
import com.haman.core.designsystem.component.SubTitle

@Composable
fun DetailScreen(
    imageId: String,
    viewModel: DetailViewModel = hiltViewModel()
) {
    LaunchedEffect(null) {
        viewModel.getImageInfo(imageId = imageId)
    }
    val detailUiState = viewModel.detailUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        when (val value = detailUiState.value) {
            DetailUiState.Error -> {}
            DetailUiState.Loading -> {}
            is DetailUiState.Success -> {
                SubTitle(
                    modifier = Modifier.padding(4.dp),
                    text = "by ${value.image.author}"
                )
                AsyncImage(
                    modifier = Modifier.fillMaxWidth(),
                    id = value.image.id,
                    load = viewModel::getImageByUrl
                )
            }
        }
    }
}