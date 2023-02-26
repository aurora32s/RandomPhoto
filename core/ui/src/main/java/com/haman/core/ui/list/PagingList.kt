package com.haman.core.ui.list

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.haman.core.common.state.ToastPosition
import com.haman.core.model.ui.UiModel
import com.haman.core.ui.item.ErrorListItem

/**
 * 페이징 데이터 처리 공통 Component
 */
@Composable
fun <T : UiModel> PagingList(
    modifier: Modifier = Modifier,
    data: LazyPagingItems<T>,
    @StringRes
    errorMsg: Int,
    toast: (ToastPosition, Int) -> Unit,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier, contentAlignment = Alignment.TopCenter) {
        when (data.loadState.refresh) {
            LoadState.Loading -> {}
            is LoadState.Error -> {
                // 초기 데이터 요청 실패
                ErrorListItem(message = errorMsg)
            }
            else -> content()
        }
        when (data.loadState.append) {
            LoadState.Loading -> {}
            is LoadState.Error -> toast(ToastPosition.BOTTOM, errorMsg)
            else -> {}
        }
        when (data.loadState.prepend) {
            LoadState.Loading -> {}
            is LoadState.Error -> toast(ToastPosition.MIDDLE, errorMsg)
            else -> {}
        }
    }
}