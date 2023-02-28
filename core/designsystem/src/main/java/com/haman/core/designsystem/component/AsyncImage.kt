package com.haman.core.designsystem.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.haman.core.designsystem.icon.DaangnIcons
import com.haman.core.designsystem.theme.Gray200
import com.haman.core.designsystem.theme.Gray700
import com.haman.core.model.ui.ImageUiModel

/**
 * 서버로 부터 이미지 Bitmap 을 받으와 보여주는 Component
 * (캐시가 있다면 캐시 사용)
 * @param id 이미지 id
 * @param cornerRadius 모서리 Radius
 * @param scaleType Image ContentScaleType
 * @param load 이미지 로드 메서드
 */
@Composable
fun AsyncImage(
    modifier: Modifier = Modifier,
    image: ImageUiModel,
    cornerRadius: Float = 4f,
    scaleType: ContentScale = ContentScale.FillWidth,
    load: suspend (String, Int, Int) -> Bitmap?,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    val bitmap = produceState<LoadImageState>(initialValue = LoadImageState.Loading, key1 = Unit) {
        val result = load(image.id, image.width, image.height)
        value = result?.let { LoadImageState.Success(it) } ?: LoadImageState.Error
    }

    val imageModifier = remember {
        derivedStateOf {
            when (scaleType) {
                ContentScale.Crop -> Modifier.fillMaxSize()
                else -> Modifier.fillMaxWidth()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius.dp))
            .background(if (isDarkTheme) Gray700 else Gray200),
        contentAlignment = Alignment.Center
    ) {
        when (val result = bitmap.value) {
            is LoadImageState.Success -> Image(
                modifier = imageModifier.value
                    .clip(RoundedCornerShape(cornerRadius.dp)),
                bitmap = result.bitmap.asImageBitmap()
                    .apply { prepareToDraw() },
                contentDescription = "Image's id is ${image.id}",
                contentScale = scaleType
            )
            is LoadImageState.Error -> Image(
                modifier = Modifier.size(50.dp),
                painter = painterResource(id = DaangnIcons.logo),
                contentDescription = "Fail to load ${image.id} Image"
            )
            LoadImageState.Loading -> {}
        }
    }
}

sealed class LoadImageState {
    object Loading : LoadImageState()

    data class Success(
        val bitmap: Bitmap
    ) : LoadImageState()

    object Error : LoadImageState()
}