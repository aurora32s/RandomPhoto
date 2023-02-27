package com.haman.core.ui.item

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.haman.core.designsystem.component.AsyncImage
import com.haman.core.designsystem.component.ContentText

/**
 * LinearLayout Image List 에 사용되는 Item
 * @param modifier Modifier
 * @param imageId 이미지 Id
 * @param author 이미지를 업로드한 작가
 * @param loadImage 이미지 요청 method
 */
@Composable
fun ImageLinearItem(
    modifier: Modifier = Modifier,
    imageId: String,
    author: String,
    loadImage: suspend (String) -> Bitmap?
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(5f / 3f),
            id = imageId,
            load = loadImage
        )
        ContentText(
            modifier = Modifier.padding(8.dp),
            text = author
        )
    }
}