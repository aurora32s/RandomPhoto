package com.haman.core.designsystem.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Orange800,
    onPrimary = White,
    background = Gray900,
    onBackground = White,
    surface = Black,
    onSurface = White
)

@Composable
fun DaangnBlackTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}