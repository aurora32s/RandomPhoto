package com.haman.feature.home.ui

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.haman.core.designsystem.component.AsyncImage
import com.haman.core.designsystem.component.ContentText
import com.haman.core.designsystem.icon.DaangnIcons
import com.haman.core.model.ui.ImageUiModel
import com.haman.core.ui.item.ImageLinearItem
import com.haman.feature.home.ListType

@Composable
fun HomeImagePagingTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        ContentText(
            text = stringResource(
                id = com.haman.core.designsystem.R.string.home_paging_title
            )
        )
    }
}

@Composable
fun HomeFloatingButton(
    listType: ListType,
    images: LazyPagingItems<ImageUiModel>,
    toggleListType: (ListType) -> Unit
) {
    val isFailed = remember {
        derivedStateOf {
            images.loadState.refresh is LoadState.Error ||
                    images.loadState.append is LoadState.Error ||
                    images.loadState.prepend is LoadState.Error
        }
    }
    FloatingActionButton(
        backgroundColor = MaterialTheme.colors.primary,
        onClick = {
            if (images.loadState.refresh is LoadState.Error) {
                images.refresh()
            } else if (images.loadState.append is LoadState.Error) {
                images.retry()
            } else if (images.loadState.prepend is LoadState.Error) {
                images.retry()
            } else {
                toggleListType(
                    when (listType) {
                        ListType.LINEAR -> ListType.GRID
                        ListType.GRID -> ListType.LINEAR
                    }
                )
            }
        }) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(
                if (isFailed.value) DaangnIcons.refresh
                else {
                    when (listType) {
                        ListType.LINEAR -> DaangnIcons.gridBtn
                        ListType.GRID -> DaangnIcons.linearBtn
                    }
                }
            ),
            contentDescription = ""
        )
    }
}

@Composable
fun HomeImagePagingItem(
    image: ImageUiModel,
    listType: ListType,
    toDetail: (String, String) -> Unit,
    loadImage: suspend (String, Int, Int) -> Bitmap?
) {
    when (listType) {
        ListType.GRID -> AsyncImage(
            modifier = Modifier
                .aspectRatio(1f)
                .clickable { toDetail(image.id, image.author) },
            id = image.id,
            load = loadImage
        )
        ListType.LINEAR -> ImageLinearItem(
            modifier = Modifier.clickable {
                toDetail(image.id, image.author)
            },
            imageId = image.id,
            author = image.author,
            loadImage = loadImage
        )
    }
}