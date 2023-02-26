package com.haman.core.ui.list

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.haman.core.common.state.ToastPosition
import com.haman.core.designsystem.component.LinearProgressBar
import com.haman.core.designsystem.icon.DaangnIcons
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
            LoadState.Loading ->
                ErrorListItem(message = loadingMsg)
            is LoadState.Error -> {
                // 초기 데이터 요청 실패
                ErrorListItem(message = errorMsg)
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