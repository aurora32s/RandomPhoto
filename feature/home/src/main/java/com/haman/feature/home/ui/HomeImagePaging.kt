package com.haman.feature.home.ui

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.haman.core.common.state.ToastPosition
import com.haman.core.designsystem.R
import com.haman.core.model.ui.ImageUiModel
import com.haman.core.ui.list.PagingList
import com.haman.core.ui.state.ToolbarState
import com.haman.feature.home.ListType

/**
 * 페이징 상태에 따른 UI 처리
 */
@Composable
fun HomeImagePaging(
    modifier: Modifier = Modifier,
    images: LazyPagingItems<ImageUiModel>,
    toolbarState: ToolbarState,
    listState: LazyListState = rememberLazyListState(),
    listType: ListType,
    toast: (ToastPosition, Int) -> Unit,
    toDetail: (String) -> Unit,
    loadImage: suspend (String, Int, Int) -> Bitmap?
) {
    PagingList(
        modifier = modifier
            .graphicsLayer {
                translationY = toolbarState.height
            },
        data = images,
        errorMsg = R.string.fail_to_load_page,
        toast = toast
    ) {
        HomeImageList(
            listType = listType,
            images = images,
            listState = listState,
            contentPadding = PaddingValues(
                start = 8.dp, end = 8.dp, top = 16.dp
            ),
            spaceBy = 8f,
            toDetail = toDetail,
            loadImage = loadImage
        )
    }
}

/**
 * 실제 이미지 정보가 보이는 리스트
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeImageList(
    listType: ListType,
    images: LazyPagingItems<ImageUiModel>,
    listState: LazyListState = LazyListState(),
    contentPadding: PaddingValues = PaddingValues(),
    spaceBy: Float = 0f,
    toDetail: (String) -> Unit,
    loadImage: suspend (String, Int, Int) -> Bitmap?
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(listType.column),
        state = listState,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(spaceBy.dp),
        horizontalArrangement = Arrangement.spacedBy(spaceBy.dp)
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