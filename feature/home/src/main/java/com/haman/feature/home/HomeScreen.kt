package com.haman.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.haman.core.common.state.ToastPosition
import com.haman.core.designsystem.icon.DaangnIcons
import com.haman.core.ui.item.ImageLinearItem
import com.haman.core.ui.list.GridPagingList
import com.haman.core.designsystem.R
import com.haman.core.designsystem.component.*
import com.haman.core.designsystem.util.ImageType
import com.haman.core.ui.list.PagingList

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
    val listType = viewModel.listType.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                onClick = { viewModel.toggleListType() }
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(
                        id = if (images.loadState.refresh is LoadState.Error) {
                            DaangnIcons.refresh
                        } else {
                            when (listType.value) {
                                ListType.LINEAR -> DaangnIcons.gridBtn
                                ListType.GRID -> DaangnIcons.linearBtn
                            }
                        }
                    ),
                    contentDescription = ""
                )
            }
        }
    ) { innerPadding ->
        val toolbarHeightRange = with(LocalDensity.current) {
            minHeightToolbar.roundToPx()..maxHeightToolbar.roundToPx()
        }
        // toolbar 상태 정보
        val toolbarState = rememberToolbarState(toolbarHeightRange = toolbarHeightRange)
        val listState = rememberLazyListState()

        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                /**
                 * Scroll 이 발생하였을 떄
                 */
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    toolbarState.scrollTopLimitReached =
                        listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
                    toolbarState.scrollOffset = toolbarState.scrollOffset - available.y
                    return Offset(0f, toolbarState.consumed)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .nestedScroll(nestedScrollConnection)
        ) {
            CollapsingToolbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        with(LocalDensity.current) { toolbarState.height.toDp() }
                    ),
                imageType = ImageType.DrawableImage(id = R.drawable.background),
                state = toolbarState.progress
            )

            PagingList(
                data = images,
                errorMsg = R.string.fail_to_load_page,
                toast = toast
            ) {
                GridPagingList(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            translationY = toolbarState.height
                        },
                    cell = listType.value.column,
                    contentPadding = PaddingValues(
                        bottom = minHeightToolbar,
                        start = 8.dp,
                        end = 8.dp,
                        top = 16.dp
                    ),
                    space = 8f,
                    data = images,
                    listState = listState,
                    title = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            ContentText(text = "오늘의 갤러리")
                        }
                    }
                ) {
                    when (listType.value) {
                        ListType.GRID -> AsyncImage(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable { toDetail(it.id) },
                            id = it.id,
                            load = viewModel::getImageByUrl
                        )
                        ListType.LINEAR -> ImageLinearItem(
                            modifier = Modifier.clickable {
                                toDetail(it.id)
                            },
                            imageId = it.id,
                            author = it.author,
                            loadImage = viewModel::getImageByUrl
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberToolbarState(toolbarHeightRange: IntRange): ToolbarState {
    return rememberSaveable(saver = ExitUnitCollapsedState.Saver) {
        ExitUnitCollapsedState(toolbarHeightRange)
    }
}