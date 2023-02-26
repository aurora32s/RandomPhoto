package com.haman.feature.home

import android.graphics.Bitmap
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.haman.core.common.state.ToastPosition
import com.haman.core.designsystem.icon.DaangnIcons
import com.haman.core.model.ui.ImageUiModel
import com.haman.core.ui.state.ToolbarState
import com.haman.core.ui.state.rememberToolbarState
import com.haman.feature.home.ui.HomeFloatingButton
import com.haman.feature.home.ui.HomeImagePaging
import com.haman.feature.home.ui.HomeToolbar

private val minHeightToolbar = 54.dp
private val maxHeightToolbar = 340.dp

/**
 * Home(Main) 화면
 * @param toDetail 이미지 클릭 시, 상세 화면으로 이동
 */
@Composable
fun HomeScreen(
    toDetail: (String) -> Unit,
    toast: (ToastPosition, Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val images = viewModel.imagesInfo.collectAsLazyPagingItems()
    // Toolbar 가능 높이
    val toolbarHeightRange = with(LocalDensity.current) {
        minHeightToolbar.roundToPx()..maxHeightToolbar.roundToPx()
    }

    val toolbarState = rememberToolbarState(toolbarHeightRange = toolbarHeightRange)
    val listState = rememberLazyListState()
    val listType = remember { mutableStateOf(ListType.GRID) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            // Scroll 이 발생하였을 떄
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                toolbarState.scrollTopLimitReached =
                    listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
                toolbarState.scrollOffset = toolbarState.scrollOffset - available.y
                return Offset(0f, toolbarState.consumed)
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            HomeFloatingButton(
                listType = listType.value,
                images = images,
                toggleListType = { listType.value = it }
            )
        }
    ) {
        HomeBody(
            images = images,
            toolbarState = toolbarState,
            listState = listState,
            listType = listType.value,
            nestedScrollConnection = nestedScrollConnection,
            toast = toast,
            toDetail = toDetail,
            loadImage = viewModel::getImageByUrl
        )
    }
}

@Composable
fun HomeBody(
    modifier: Modifier = Modifier,
    images: LazyPagingItems<ImageUiModel>,
    toolbarState: ToolbarState,
    listState: LazyListState,
    listType: ListType,
    nestedScrollConnection: NestedScrollConnection,
    toast: (ToastPosition, Int) -> Unit,
    toDetail: (String) -> Unit,
    loadImage: suspend (String, Int, Int) -> Bitmap?
) {
    Box(modifier = modifier.nestedScroll(nestedScrollConnection)) {
        HomeToolbar(toolbarState = toolbarState)
        HomeImagePaging(
            modifier = Modifier.nestedScroll(nestedScrollConnection),
            images = images,
            toolbarState = toolbarState,
            listState = listState,
            listType = listType,
            toast = toast,
            toDetail = toDetail,
            loadImage = loadImage
        )
    }
}