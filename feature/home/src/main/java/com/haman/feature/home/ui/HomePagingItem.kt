package com.haman.feature.home.ui

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.haman.core.common.extension.fromDpToPx
import com.haman.core.designsystem.R
import com.haman.core.designsystem.component.AsyncImage
import com.haman.core.designsystem.component.ContentText
import com.haman.core.designsystem.icon.DaangnIcons
import com.haman.core.model.ui.ImageUiModel
import com.haman.core.ui.item.ImageLinearItem
import com.haman.feature.home.ListType

/**
 * 이미지 리스트 위에 띄워줄 Title Component
 */
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
                id = R.string.home_paging_title
            )
        )
    }
}

/**
 * 이미지 리스트 타입 변경 또는 Refresh 할 수 있는 FAB
 */
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
            contentDescription = stringResource(
                if (isFailed.value)
                    R.string.home_floating_action_button_for_refresh
                else
                    R.string.home_floating_action_button_for_changing_type
            )
        )
    }
}

/**
 * 이미지 리스트 타입에 따른 리스트 Item
 * @param listType Grid or Linear
 * @param toDetail 상세 화면으로 이동하는 Event
 * @param loadImage 서버로부터(또는 캐시) 이미지를 받아오는 Event
 */
@Composable
fun HomeImagePagingItem(
    image: ImageUiModel,
    listType: ListType,
    toDetail: (String, String) -> Unit,
    loadImage: suspend (String, Int, Int, Int) -> Bitmap?
) {
    val localScreenWidth = LocalConfiguration.current.screenWidthDp.toFloat().fromDpToPx()
    val imageWidth = remember { derivedStateOf { localScreenWidth / listType.column } }
    when (listType) {
        ListType.GRID -> AsyncImage(
            modifier = Modifier
                .aspectRatio(1f)
                .clickable { toDetail(image.id, image.author) },
            image = image,
            width = imageWidth.value,
            loadImage = loadImage,
            scaleType = ContentScale.Crop
        )
        ListType.LINEAR -> ImageLinearItem(
            modifier = Modifier.clickable {
                toDetail(image.id, image.author)
            },
            image = image,
            author = image.author,
            loadImage = loadImage
        )
    }
}