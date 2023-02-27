package com.haman.feature.home

import android.graphics.Bitmap
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.haman.core.common.state.ToastPosition
import com.haman.core.designsystem.R
import com.haman.core.model.ui.ImageUiModel
import com.haman.core.ui.list.PagingList
import com.haman.core.ui.state.ToolbarState
import com.haman.core.ui.state.rememberToolbarState
import com.haman.feature.home.ui.HomeFloatingButton
import com.haman.feature.home.ui.HomeImagePaging
import com.haman.feature.home.ui.HomeToolbar

/**
 * Toolbar 높이 범위
 */
private val minHeightToolbar = 54.dp
private val maxHeightToolbar = 340.dp

/**
 * Home(Main) 화면
 * @param toDetail 이미지 클릭 시, 상세 화면으로 이동
 * @param toast Toast 띄우기 Event
 * @param completeLoadInitData 초기 데이터 요청 성공 시의 Event
 */
@Composable
fun HomeScreen(
    toDetail: (String, String) -> Unit,
    toast: (ToastPosition, Int) -> Unit,
    completeLoadInitData: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val images = viewModel.imagesInfo.collectAsLazyPagingItems()

    LaunchedEffect(key1 = images.loadState.refresh) {
        // 최초의 데이터 요청
        if (images.loadState.refresh != LoadState.Loading) {
            completeLoadInitData()
        }
    }

    // Toolbar 가능 높이
    val toolbarHeightRange = with(LocalDensity.current) {
        minHeightToolbar.roundToPx()..maxHeightToolbar.roundToPx()
    }

    val toolbarState = rememberToolbarState(toolbarHeightRange = toolbarHeightRange)
    val listState = rememberLazyListState()
    val listType = rememberSaveable { mutableStateOf(ListType.GRID) }

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
        PagingList(
            modifier = Modifier
                .nestedScroll(nestedScrollConnection),
            data = images,
            errorMsg = R.string.fail_to_load_page,
            loadingMsg = R.string.loading_msg,
            toast = toast
        ) {
            HomeBody(
                images = images,
                toolbarState = toolbarState,
                listState = listState,
                listType = listType.value,
                toDetail = toDetail,
                loadImage = viewModel::getImageByUrl
            )
        }
    }
}

@Composable
fun HomeBody(
    images: LazyPagingItems<ImageUiModel>,
    toolbarState: ToolbarState,
    listState: LazyListState,
    listType: ListType,
    toDetail: (String, String) -> Unit,
    loadImage: suspend (String) -> Bitmap?
) {
    HomeToolbar(toolbarState = toolbarState)
    HomeImagePaging(
        modifier = Modifier
            .graphicsLayer {
                translationY = toolbarState.height
            },
        images = images,
        listState = listState,
        listType = listType,
        toDetail = toDetail,
        loadImage = loadImage
    )
}