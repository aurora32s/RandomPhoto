package com.haman.feature.home.ui

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.haman.core.model.ui.ImageUiModel
import com.haman.feature.home.ListType

/**
 * 실제 이미지 정보가 보이는 Paging List
 * @param images 이미지 Paging data
 * @param listState list state
 * @param listType 현재 리스트 타입 (Grid, Linear)
 * @param toDetail 상세 화면으로 이동 Event
 * @param loadImage 서버로부터(또는 캐싱) 이미지 로드 Event
 */
@Composable
fun HomeImagePaging(
    modifier: Modifier = Modifier,
    images: LazyPagingItems<ImageUiModel>,
    listState: LazyListState = rememberLazyListState(),
    listType: ListType,
    toDetail: (String, ImageUiModel) -> Unit,
    loadImage: suspend (String, Int, Int, Int) -> Bitmap?
) {
    if (images.itemCount != 0) {
        LazyColumn(
            modifier = modifier,
            state = listState,
            contentPadding = PaddingValues(
                start = 8.dp, end = 8.dp, top = 16.dp, bottom = 160f.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8f.dp)
        ) {
            item { HomeImagePagingTitle() }
            imageItems(
                items = images,
                listType = listType,
                toDetail = toDetail,
                loadImage = loadImage
            )
        }
    }
}