package com.haman.core.ui.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.haman.core.model.ui.UiModel

@Composable
fun <T : UiModel> VerticalPagingList(
    data: LazyPagingItems<T>,
    item: @Composable (T) -> Unit
) {
    LazyColumn {
        items(items = data, key = { it.id }) {
            it?.let { item(it) }
        }
    }
}