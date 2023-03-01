package com.haman.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import com.haman.core.designsystem.util.ImageType

@Composable
fun CollapsingToolbar(
    modifier: Modifier,
    imageType: ImageType,
    height: Float,
    progress: Float,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .height(with(LocalDensity.current) { height.toDp() }),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            color = MaterialTheme.colors.surface.copy(alpha = progress)
        ) {
            when (imageType) {
                is ImageType.DrawableImage -> Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { alpha = progress * 0.75f },
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = imageType.id),
                    contentDescription = ""
                )
                is ImageType.AsyncImage -> imageType.image.let {
                    AsyncImage(image = it, loadImage = imageType.load)
                }
            }
            content()
        }
    }
}