package com.haman.core.ui.list

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.haman.core.common.state.ToastPosition
import com.haman.core.designsystem.component.LinearProgressBar
import com.haman.core.model.ui.UiModel
import com.haman.core.ui.item.ErrorMessageText

/**
 * 페이징 데이터 처리 공통 Component
 * @param data 페이지 데이터
 * @param errorMsg 에러가 발생했을 때 보여줄 메시지 resource id
 * @param loadingMsg 로딩 중에 보여줄 메시지 resource id
 * @param toast 에러가 발생했을 때 보여줄 toast
 * @param content 실질적으로 보여줄 content
 */
@Composable
fun <T : UiModel> PagingList(
    modifier: Modifier = Modifier,
    data: LazyPagingItems<T>,
    @StringRes
    errorMsg: Int,
    @StringRes
    loadingMsg: Int,
    toast: (ToastPosition, Int) -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        when (data.loadState.refresh) {
            // 초기 데이터 로딩 중
            LoadState.Loading -> ErrorMessageText(message = loadingMsg)
            is LoadState.Error -> {
                // 초기 데이터 요청 실패
                ErrorMessageText(message = errorMsg)
            }
            else -> content()
        }
        when (data.loadState.append) {
            LoadState.Loading ->
                LinearProgressBar(modifier = Modifier.align(Alignment.BottomCenter))
            is LoadState.Error -> toast(ToastPosition.BOTTOM, errorMsg)
            else -> {}
        }
        when (data.loadState.prepend) {
            LoadState.Loading ->
                LinearProgressBar(modifier = Modifier.align(Alignment.TopCenter))
            is LoadState.Error -> toast(ToastPosition.MIDDLE, errorMsg)
            else -> {}
        }
    }
}