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
 * 실제 이미지 정보가 보이는 리스트
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeImagePaging(
    modifier: Modifier = Modifier,
    images: LazyPagingItems<ImageUiModel>,
    listState: LazyListState = rememberLazyListState(),
    listType: ListType,
    toDetail: (String, String) -> Unit,
    loadImage: suspend (String, Int, Int) -> Bitmap?
) {
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
        items(images.itemCount) { index ->
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