package com.haman.core.ui.item

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.haman.core.common.extension.fromDpToPx
import com.haman.core.designsystem.component.AsyncImage
import com.haman.core.model.ui.ImageUiModel
import kotlin.math.min

/**
 * LinearLayout Image List 에 사용되는 Item
 * @param modifier Modifier
 * @param images 가로로 보여줄 이미지 리스트
 * @param cell 가로 열 개수
 * @param startIndex 가로 열 첫 index
 * @param toDetail 클릭한 이미지의 세부 화면으로 이동
 * @param loadImage 이미지 요청 method
 */
@Composable
fun GridImageItem(
    modifier: Modifier = Modifier,
    images: LazyPagingItems<ImageUiModel>,
    cell: Int = 1,
    startIndex: Int,
    toDetail: (String, ImageUiModel) -> Unit,
    loadImage: suspend (String, Int, Int, Int) -> Bitmap?
) {
    val localScreenWidth = LocalConfiguration.current.screenWidthDp.toFloat().fromDpToPx()
    val imageWidth = remember { derivedStateOf { localScreenWidth / cell } }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8f.dp)
    ) {
        (startIndex until min(startIndex + cell, images.itemCount)).forEach {
            images[it]?.let { image ->
                AsyncImage(
                    modifier = modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clickable { toDetail(image.id, image) },
                    image = image,
                    width = imageWidth.value,
                    loadImage = loadImage,
                    scaleType = ContentScale.Crop
                )
            }
        }
    }
}