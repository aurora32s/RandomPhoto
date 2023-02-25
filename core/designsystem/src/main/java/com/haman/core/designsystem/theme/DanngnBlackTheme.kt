package com.haman.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.ColorPainter

private val ColorPalette = darkColors(
    primary = Orange800,
    onPrimary = White,
    background = Gray900,
    onBackground = White,
    surface = Black,
    onSurface = White
)

@Composable
fun DaangnBlackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = ColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}