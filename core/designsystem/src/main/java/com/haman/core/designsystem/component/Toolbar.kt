package com.haman.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.haman.core.designsystem.icon.DaangnIcons
import com.haman.core.designsystem.util.ImageType
import com.haman.core.designsystem.R

@Stable
interface ToolbarState {
    val height: Float
    val progress: Float
    val consumed: Float
    var scrollTopLimitReached: Boolean
    var scrollOffset: Float
}

abstract class ScrollFlagState(heightRange: IntRange) : ToolbarState {
    init {
        require(heightRange.first >= 0 && heightRange.last >= heightRange.first) {
            "Illegal State 0 <= min height < max height"
        }
    }

    protected val minHeight = heightRange.first
    protected val maxHeight = heightRange.last
    protected val rangeOfDiff = maxHeight - minHeight
    protected var _consumed = 0f
    protected abstract var _scrollOffset: Float

    final override val height: Float
        get() = (maxHeight - scrollOffset).coerceIn(minHeight.toFloat(), maxHeight.toFloat())

    final override val progress: Float
        get() = 1 - (maxHeight - height) / rangeOfDiff

    final override val consumed: Float
        get() = _consumed

    final override var scrollTopLimitReached = true
}

class ExitUnitCollapsedState(
    heightRange: IntRange,
    scrollOffset: Float = 0f
) : ScrollFlagState(heightRange) {
    // backing property
    override var _scrollOffset by mutableStateOf(
        value = scrollOffset.coerceIn(0f, rangeOfDiff.toFloat()),
        policy = structuralEqualityPolicy()
    )
    override var scrollOffset: Float
        get() = _scrollOffset
        set(value) {
            if (scrollTopLimitReached) {
                val oldOffset = _scrollOffset
                _scrollOffset = value.coerceIn(0f, rangeOfDiff.toFloat())
                _consumed = oldOffset - _scrollOffset
            } else {
                _consumed = 0f
            }
        }

    companion object {
        private const val MIN_HEIGHT = "MIN_HEIGHT"
        private const val MAX_HEIGHT = "MAX_HEIGHT"
        private const val SCROLL_OFFSET = "SCROLL_OFFSET"

        val Saver: Saver<ExitUnitCollapsedState, *> = mapSaver(
            save = {
                mapOf(
                    MIN_HEIGHT to it.minHeight,
                    MAX_HEIGHT to it.maxHeight,
                    SCROLL_OFFSET to it.scrollOffset
                )
            },
            restore = {
                ExitUnitCollapsedState(
                    heightRange = (it[MIN_HEIGHT] as Int)..(it[MAX_HEIGHT] as Int),
                    scrollOffset = it[SCROLL_OFFSET] as Float
                )
            }
        )
    }
}

@Composable
fun CollapsingToolbar(
    modifier: Modifier,
    imageType: ImageType,
    state: Float
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(bottomStart = 16.dp * state, bottomEnd = 16.dp * state))
            .background(
                color = MaterialTheme.colors.background.copy(alpha = 1 - state)
            ),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            color = MaterialTheme.colors.surface.copy(alpha = state)
        ) {
            when (imageType) {
                is ImageType.BitmapImage -> Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { alpha = state * 0.75f },
                    bitmap = imageType.bitmap.asImageBitmap(),
                    contentDescription = ""
                )
                is ImageType.DrawableImage -> Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { alpha = state * 0.75f },
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = imageType.id),
                    contentDescription = ""
                )
                is ImageType.AsyncImage -> imageType.imageId?.let {
                    AsyncImage(id = it, load = imageType.load)
                }
            }
            DaangnToolBarLayout(
                modifier = Modifier.padding(8.dp),
                state = state
            ) {
                Image(
                    modifier = Modifier.size(50.dp),
                    painter = painterResource(id = DaangnIcons.logo),
                    contentDescription = ""
                )
                SubTitle(text = stringResource(id = R.string.app_title))
            }
//            Column(
//                modifier = Modifier.fillMaxSize(),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//            }
        }
    }
}

@Composable
fun DaangnToolBarLayout(
    modifier: Modifier = Modifier,
    state: Float,
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

            logo.placeRelative(
                x = lerp(
                    start = ((constraints.maxWidth - totalWidth) / 2).toDp(),
                    stop = ((constraints.maxWidth - logo.measuredWidth) / 2).toDp(),
                    fraction = state
                ).roundToPx(),
                y = lerp(
                    start = ((constraints.maxHeight - logo.measuredHeight) / 2).toDp(),
                    stop = ((constraints.maxHeight / 2 - logo.measuredHeight)).toDp(),
                    fraction = state
                ).roundToPx()
            )
            title.placeRelative(
                x = lerp(
                    start = ((constraints.maxWidth - totalWidth) / 2 + logo.measuredWidth * 3 / 4).toDp(),
                    stop = ((constraints.maxWidth - title.measuredWidth) / 2).toDp(),
                    fraction = state
                ).roundToPx(),
                y = lerp(
                    start = ((constraints.maxHeight - title.measuredHeight) / 2 + 10).toDp(),
                    stop = (constraints.maxHeight / 2).toDp(),
                    fraction = state
                ).roundToPx()
            )
        }
    }
}