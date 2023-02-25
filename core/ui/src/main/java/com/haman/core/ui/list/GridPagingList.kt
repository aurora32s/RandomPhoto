package com.haman.core.ui.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.haman.core.model.ui.UiModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : UiModel> GridPagingList(
    cell: Int = 2,
    data: LazyPagingItems<T>,
    item: @Composable (T) -> Unit
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(cell)
    ) {
        items(data.itemCount) { index ->
            data[index]?.let { item(it) }
        }
    }
}