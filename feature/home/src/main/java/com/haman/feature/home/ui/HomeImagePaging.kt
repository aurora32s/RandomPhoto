package com.haman.feature.home.ui

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.haman.core.model.ui.ImageUiModel
import com.haman.feature.home.ListType
import kotlin.math.min

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
            items(count = images.itemCount / listType.column) { index ->
                when (listType) {
                    ListType.LINEAR -> {
                        images[index]?.let {
                            HomeImagePagingItem(
                                image = it,
                                listType = listType,
                                toDetail = toDetail,
                                loadImage = loadImage
                            )
                        }
                    }
                    ListType.GRID -> {
                        GridImageItem(
                            images = images,
                            startIndex = index * 3,
                            listType = listType,
                            toDetail = toDetail,
                            loadImage = loadImage
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GridImageItem(
    images: LazyPagingItems<ImageUiModel>,
    startIndex: Int,
    listType: ListType,
    toDetail: (String, ImageUiModel) -> Unit,
    loadImage: suspend (String, Int, Int, Int) -> Bitmap?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8f.dp)
    ) {
        (startIndex until min(startIndex + 3, images.itemCount)).forEach {
            images[it]?.let { image ->
                HomeImagePagingItem(
                    modifier = Modifier.weight(1f),
                    image = image,
                    listType = listType,
                    toDetail = toDetail,
                    loadImage = loadImage
                )
            }
        }
    }
}