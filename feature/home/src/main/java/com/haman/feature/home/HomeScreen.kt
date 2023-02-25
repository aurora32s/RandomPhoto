package com.haman.feature.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.haman.core.designsystem.component.AsyncImage
import com.haman.core.designsystem.icon.DaangnIcons
import com.haman.core.ui.item.ImageLinearItem
import com.haman.core.ui.list.GridPagingList
import com.haman.core.ui.list.PagingList
import com.haman.core.ui.list.VerticalPagingList

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val images = viewModel.imagesInfo.collectAsLazyPagingItems()
    val listType = viewModel.listType.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.toggleListType() }) {
                Icon(
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
            when (listType.value) {
                ListType.LINEAR -> VerticalPagingList(data = images) {
                    ImageLinearItem(
                        imageId = it.id,
                        author = it.author,
                        loadImage = viewModel::getImageByUrl
                    )
                }
                ListType.GRID -> GridPagingList(data = images) {
                    AsyncImage(id = it.id, load = viewModel::getImageByUrl)
                }
            }
        }
    }
}