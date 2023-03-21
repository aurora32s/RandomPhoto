package com.haman.feature.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.haman.core.designsystem.component.AsyncImage
import com.haman.core.designsystem.component.SubTitle
import com.haman.core.designsystem.icon.RandomIcons
import com.haman.core.designsystem.theme.RandomPhotoTheme
import com.haman.core.model.ui.ImageUiModel

/**
 * 이미지의 상세 정보를 보여줄 Screen
 * @param imageId 이미지 id
 * @param image 해당 이미지 정보
 * @param onBackPressed 이전 화면으로 이동
 */
@Composable
fun DetailScreen(
    imageId: String?,
    image: ImageUiModel?,
    onBackPressed: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val imageState = remember { mutableStateOf(image) }
    LaunchedEffect(null) {
        // 전달된 image 정보가 없는 경우 다시 서버에 요청
        if (image == null) {
            if (imageId != null) imageState.value = viewModel.getImageInfo(imageId = imageId)
            else onBackPressed()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
                .size(24.dp)
                .clickable { onBackPressed() },
            tint = RandomPhotoTheme.colors.icon,
            painter = painterResource(id = RandomIcons.close),
            contentDescription = "close button"
        )
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (imageState.value?.author != null) {
                SubTitle(
                    modifier = Modifier.padding(8.dp),
                    text = "by ${imageState.value?.author}"
                )
            }
            imageState.value?.let {
                AsyncImage(
                    image = it,
                    loadImage = viewModel::getImageByUrl,
                    scaleType = ContentScale.FillWidth
                )
            }
        }
    }
}