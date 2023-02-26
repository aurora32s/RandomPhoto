package com.haman.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.haman.core.designsystem.R
import com.haman.core.designsystem.component.CollapsingToolbar
import com.haman.core.designsystem.component.SubTitle
import com.haman.core.designsystem.icon.DaangnIcons
import com.haman.core.designsystem.util.ImageType
import com.haman.core.ui.state.ToolbarState

/**
 * HomeScreen 에서 쓰이는
 */
@Composable
fun HomeToolbar(
    toolbarState: ToolbarState
) {
    val progress = remember { derivedStateOf { toolbarState.progress } }
    CollapsingToolbar(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    bottomStart = 16.dp * progress.value,
                    bottomEnd = 16.dp * progress.value
                )
            )
            .background(
                color = MaterialTheme.colors.background
                    .copy(alpha = 1 - progress.value)
            ),
        imageType = ImageType.DrawableImage(id = R.drawable.background),
        height = toolbarState.height,
        progress = progress.value
    ) {
        HomeToolBarContent(
            modifier = Modifier.padding(8.dp),
            progress = toolbarState.progress
        ) {
            Image(
                modifier = Modifier.size(50.dp),
                painter = painterResource(id = DaangnIcons.logo),
                contentDescription = ""
            )
            SubTitle(text = stringResource(id = R.string.app_title))
        }
    }
}

/**
 * HomeScreen Toolbar 에 보이는 Logo 와 Title
 */
@Composable
fun HomeToolBarContent(
    modifier: Modifier = Modifier,
    progress: Float,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight
        ) {
            val logo = placeables[0] // 로고 아이콘
            val title = placeables[1] // Title
            val totalWidth = (logo.measuredWidth / 2) + title.measuredWidth

            // 로고 위치 변경
            logo.placeRelative(
                x = lerp(
                    start = ((constraints.maxWidth - totalWidth) / 2).toDp(),
                    stop = ((constraints.maxWidth - logo.measuredWidth) / 2).toDp(),
                    fraction = progress
                ).roundToPx(),
                y = lerp(
                    start = ((constraints.maxHeight - logo.measuredHeight) / 2).toDp(),
                    stop = ((constraints.maxHeight / 2 - logo.measuredHeight)).toDp(),
                    fraction = progress
                ).roundToPx()
            )

            // Title 위치 변경
            title.placeRelative(
                x = lerp(
                    start = ((constraints.maxWidth - totalWidth) / 2 + logo.measuredWidth * 3 / 4).toDp(),
                    stop = ((constraints.maxWidth - title.measuredWidth) / 2).toDp(),
                    fraction = progress
                ).roundToPx(),
                y = lerp(
                    start = ((constraints.maxHeight - title.measuredHeight) / 2 + 10).toDp(),
                    stop = (constraints.maxHeight / 2).toDp(),
                    fraction = progress
                ).roundToPx()
            )
        }
    }
}