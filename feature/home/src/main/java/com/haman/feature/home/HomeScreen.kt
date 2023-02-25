package com.haman.feature.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.haman.core.ui.item.ImageLinearItem
import com.haman.core.ui.list.PagingList

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val images = viewModel.imagesInfo.collectAsLazyPagingItems()

    PagingList(data = images) {
        LazyColumn {
            items(items = images, key = { image -> image.id }) { image ->
                image?.let {
                    ImageLinearItem(
                        imageId = image.id,
                        author = image.author,
                        loadImage = viewModel::getImageByUrl
                    )
                }
            }
        }
    }
}