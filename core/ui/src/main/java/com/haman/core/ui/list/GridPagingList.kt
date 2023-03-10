package com.haman.core.ui.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.haman.core.model.ui.UiModel

/**
 * Paging 에 쓰이는 Grid List Layout
 * @param cell 세로 column 개수
 * @param contentPadding contentPadding
 * @param space 아이템 들 사이 space 크기
 * @param data Paging Data
 * @param listState List State
 * @param title 리스트 상단 제목
 * @param item 리스트에 들어갈 아이템 Component
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : UiModel> GridPagingList(
    modifier: Modifier = Modifier,
    cell: Int = 2,
    contentPadding: PaddingValues = PaddingValues(),
    space: Float = 0f,
    data: LazyPagingItems<T>,
    listState: LazyListState,
    title: @Composable () -> Unit = {},
    item: @Composable (T) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        cells = GridCells.Fixed(cell),
        state = listState,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(space.dp),
        horizontalArrangement = Arrangement.spacedBy(space.dp)
    ) {
        item(span = { GridItemSpan(cell) }) { title() }
        items(data.itemCount) { index ->
            data[index]?.let { item(it) }
        }
    }
}