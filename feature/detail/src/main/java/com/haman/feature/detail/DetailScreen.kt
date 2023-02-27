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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.haman.core.designsystem.component.AsyncImage
import com.haman.core.designsystem.component.SubTitle
import com.haman.core.designsystem.icon.DaangnIcons

/**
 * 이미지의 상세 정보를 보여줄 Screen
 * @param imageId 이미지 id
 * @param author 해당 이미지 작성자명
 * @param onBackPressed 이전 화면으로 이동
 */
@Composable
fun DetailScreen(
    imageId: String?,
    author: String?,
    onBackPressed: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val authorState = remember { mutableStateOf(author) }
    LaunchedEffect(null) {
        // 전달된 author 정보가 없는 경우에만 다시 서버로 요청하기
        if (author == null) authorState.value = viewModel.getImageInfo(imageId = imageId)
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
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (authorState.value != null) {
                SubTitle(
                    modifier = Modifier.padding(8.dp),
                    text = "by ${authorState.value}"
                )
            }
            // 이미지는 캐싱된 거 보여주기
            if (imageId != null) {
                AsyncImage(
                    id = imageId,
                    load = viewModel::getImageByUrl
                )
            }
        }
    }
}