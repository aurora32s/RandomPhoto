package com.haman.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.haman.core.designsystem.component.AsyncImage
import com.haman.core.designsystem.icon.DaangnIcons
import com.haman.core.ui.item.ImageLinearItem
import com.haman.core.ui.list.GridPagingList
import com.haman.core.ui.list.PagingList

/**
 * Home(Main) 화면
 * @param toDetail 이미지 클릭 시, 상세 화면으로 이동
 */
@Composable
fun HomeScreen(
    toDetail: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val images = viewModel.imagesInfo.collectAsLazyPagingItems()
    val listType = viewModel.listType.collectAsState()
    val listState = rememberLazyListState()

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
                        id = when (listType.value) {
                            ListType.LINEAR -> DaangnIcons.gridBtn
                            ListType.GRID -> DaangnIcons.linearBtn
                        }
                    ),
                    contentDescription = ""
                )
            }
        }
    ) {
        PagingList(data = images) {
            GridPagingList(
                cell = listType.value.column,
                contentPadding = 8f,
                data = images,
                listState = listState
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