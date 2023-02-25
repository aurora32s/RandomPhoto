package com.haman.core.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.haman.core.model.ui.UiModel

/**
 * 페이징 데이터 처리 공통 Component
 */
@Composable
fun <T : UiModel> PagingList(
    data: LazyPagingItems<T>,
    content: @Composable () -> Unit
) {
    when (data.loadState.refresh) {
        LoadState.Loading -> {}
        is LoadState.Error -> {
            // 초기 데이터 요청 실패
            if (data.itemCount == 0) {
                // TODO 에러 아이템 출력
            } else { // 페이징 데이터 실패
                // TODO 에러 Toast 출력
            }
        }
        else -> content()
    }
}