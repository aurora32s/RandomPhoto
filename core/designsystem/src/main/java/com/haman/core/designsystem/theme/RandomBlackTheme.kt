package com.haman.core.designsystem.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable

private val ColorPalette = darkColors(
    primary = Orange800,
    onPrimary = White,
    background = Gray900,
    onBackground = White,
    surface = Black,
    onSurface = White
)

@Composable
fun RandomBlackTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = ColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}