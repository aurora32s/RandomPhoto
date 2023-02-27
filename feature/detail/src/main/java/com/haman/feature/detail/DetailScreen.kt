package com.haman.feature.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.haman.core.designsystem.R
import com.haman.core.designsystem.component.AsyncImage
import com.haman.core.designsystem.component.LinearProgressBar
import com.haman.core.designsystem.component.SubTitle
import com.haman.core.designsystem.icon.DaangnIcons
import com.haman.core.ui.item.ErrorListItem

@Composable
fun DetailScreen(
    imageId: String?,
    onBackPressed: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val detailUiState = viewModel.detailUiState.collectAsState()
    LaunchedEffect(null) {
        viewModel.getImageInfo(imageId = imageId)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
                .clickable { onBackPressed() },
            painter = painterResource(id = DaangnIcons.close),
            contentDescription = "close button"
        )
        when (val value = detailUiState.value) {
            DetailUiState.Error ->
                ErrorListItem(message = R.string.fail_to_load_detail)
            DetailUiState.Loading ->
                LinearProgressBar(modifier = Modifier.align(Alignment.BottomCenter))
            is DetailUiState.Success ->
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(vertical = 24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    SubTitle(
                        modifier = Modifier.padding(8.dp),
                        text = "by ${value.image.author}"
                    )
                    if (imageId != null) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 260.dp),
                            id = imageId,
                            load = viewModel::getImageByUrl
                        )
                    }
                }
        }
    }
}