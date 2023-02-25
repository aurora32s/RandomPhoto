package com.haman.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Orange800,
    onPrimary = White,
    background = Gray900,
    onBackground = White,
    surface = Black,
    onSurface = White
)

private val LightColorPalette = lightColors(
    primary = Orange800,
    onPrimary = White,
    background = White,
    onBackground = Gray600,
    surface = Black,
    onSurface = White
)

@Composable
fun DaangnPhotoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}