package com.haman.feature.home

import android.graphics.Bitmap
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.haman.core.common.state.UiEvent
import com.haman.core.designsystem.R
import com.haman.core.model.ui.ImageUiModel
import com.haman.core.ui.list.PagingList
import com.haman.core.ui.state.ToolbarState
import com.haman.core.ui.state.rememberToolbarState
import com.haman.feature.home.ui.HomeFloatingButton
import com.haman.feature.home.ui.HomeImagePaging
import com.haman.feature.home.ui.HomeToolbar
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Toolbar 높이 범위
 */
private val minHeightToolbar = 54.dp
private val maxHeightToolbar = 340.dp

/**
 * Home(Main) 화면
 * @param uiEvent MainViewModel 에 있는 전체 Ui Event Flow 관리
 * @param toDetail 이미지 클릭 시, 상세 화면으로 이동
 */
@Composable
fun HomeScreen(
    uiEvent: MutableSharedFlow<UiEvent>,
    toDetail: (String, ImageUiModel) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val images = viewModel.imagesInfo.collectAsLazyPagingItems()
    val listType = viewModel.listType.collectAsState()
    val coroutine = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = images.loadState.refresh) {
        // 최초의 데이터 요청
        if (images.loadState.refresh != LoadState.Loading) {
            uiEvent.emit(UiEvent.CompleteLoadInitData)
        }
    }
    // Toolbar 가능 높이

    val toolbarState = rememberToolbarState(toolbarHeightRangeProvider = {
        with(LocalDensity.current) {
            minHeightToolbar.roundToPx()..maxHeightToolbar.roundToPx()
        }
    })

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
                toggleListType = viewModel::setListType
            )
        }
    ) { innerPadding ->
        PagingList(
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(nestedScrollConnection),
            data = images,
            errorMsg = R.string.fail_to_load_page,
            loadingMsg = R.string.loading_msg,
            toast = { position, message ->
                coroutine.launch {
                    uiEvent.emit(UiEvent.Toast(position = position, message = message))
                }
            }
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
    toDetail: (String, ImageUiModel) -> Unit,
    loadImage: suspend (String, Int, Int, Int) -> Bitmap?
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