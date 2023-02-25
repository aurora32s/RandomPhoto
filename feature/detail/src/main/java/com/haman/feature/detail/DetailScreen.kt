package com.haman.feature.detail

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.haman.core.designsystem.component.AsyncImage
import com.haman.core.designsystem.component.SubTitle
import com.haman.core.designsystem.icon.DaangnIcons
import com.haman.core.model.ui.ImageUiModel

@Composable
fun DetailScreen(
    imageId: String,
    onBackPressed: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    LaunchedEffect(null) {
        viewModel.getImageInfo(imageId = imageId)
    }
    val detailUiState = viewModel.detailUiState.collectAsState()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onBackPressed() },
                    painter = painterResource(id = DaangnIcons.close),
                    contentDescription = "close button"
                )
            }
        }
    ) {
        when (val value = detailUiState.value) {
            DetailUiState.Error -> onBackPressed()
            DetailUiState.Loading -> {}
            is DetailUiState.Success -> DetailBody(
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        SubTitle(
            modifier = Modifier.padding(8.dp),
            text = "by ${image.author}"
        )
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 260.dp),
            id = image.id,
            load = loadImage
        )
    }
}