package com.haman.feature.home.ui

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.*
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
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeImagePaging(
    modifier: Modifier = Modifier,
    images: LazyPagingItems<ImageUiModel>,
    listState: LazyListState = rememberLazyListState(),
    listType: ListType,
    toDetail: (String, String) -> Unit,
    loadImage: suspend (String, Int, Int, Int) -> Bitmap?
) {
    if (images.itemCount != 0) {
        LazyVerticalGrid(
            modifier = modifier,
            cells = GridCells.Fixed(listType.column),
            state = listState,
            contentPadding = PaddingValues(
                start = 8.dp, end = 8.dp, top = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8f.dp),
            horizontalArrangement = Arrangement.spacedBy(8f.dp)
        ) {
            item(span = { GridItemSpan(listType.column) }) { HomeImagePagingTitle() }
            items(count = images.itemCount) { index ->
                images[index]?.let {
                    HomeImagePagingItem(
                        image = it,
                        listType = listType,
                        toDetail = toDetail,
                        loadImage = loadImage
                    )
                }
            }
        }
    }
}