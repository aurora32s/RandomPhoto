package com.haman.core.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorPalette = RandomPhotoColors(
    primary = Orange800,
    onPrimary = White,
    background = Gray900,
    text = White,
    icon = White,
    dim = Black,
    onDim = White,
    imageThumb = Gray700,
    isDark = true
)

private val LightColorPalette = RandomPhotoColors(
    primary = Orange800,
    onPrimary = White,
    background = White,
    text = Black,
    icon = Black,
    dim = Black,
    onDim = White,
    imageThumb = Gray200,
    isDark = false
)

@Composable
fun RandomPhotoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                darkTheme.not()
        }
    }

    CompositionLocalProvider(
        LocalRandomColors provides colors
    ) {
        MaterialTheme(
            colors = debugColor(darkTheme, colors),
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

object RandomPhotoTheme {
    val colors: RandomPhotoColors
        @Composable
        get() = LocalRandomColors.current
}