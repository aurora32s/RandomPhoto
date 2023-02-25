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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : UiModel> GridPagingList(
    modifier: Modifier = Modifier,
    cell: Int = 2,
    contentPadding: Float = 0f,
    data: LazyPagingItems<T>,
    listState: LazyListState,
    item: @Composable (T) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        cells = GridCells.Fixed(cell),
        state = listState,
        contentPadding = PaddingValues(contentPadding.dp),
        verticalArrangement = Arrangement.spacedBy(contentPadding.dp),
        horizontalArrangement = Arrangement.spacedBy(contentPadding.dp)
    ) {
        items(data.itemCount) { index ->
            data[index]?.let { item(it) }
        }
    }
}