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

@Stable
class RandomPhotoColors(
    primary: Color,
    onPrimary: Color,
    background: Color,
    text: Color,
    icon: Color,
    dim: Color,
    onDim: Color,
    imageThumb: Color,
    isDark: Boolean
) {
    var primary by mutableStateOf(primary)
        private set
    var onPrimary by mutableStateOf(onPrimary)
        private set
    var background by mutableStateOf(background)
        private set
    var text by mutableStateOf(text)
        private set
    var icon by mutableStateOf(icon)
        private set
    var dim by mutableStateOf(dim)
        private set
    var onDim by mutableStateOf(onDim)
        private set
    var imageThumb by mutableStateOf(imageThumb)
        private set
    var isDark by mutableStateOf(isDark)
        private set

    fun update(other: RandomPhotoColors) {
        this.primary = other.primary
        this.onPrimary = other.onPrimary
        this.background = other.background
        this.text = other.text
        this.icon = other.icon
        this.dim = other.dim
        this.onDim = other.onDim
        this.imageThumb = other.imageThumb
        this.isDark = other.isDark
    }

    fun copy() = RandomPhotoColors(
        primary = this.primary,
        onPrimary = this.onPrimary,
        background = this.background,
        text = this.text,
        icon = this.icon,
        dim = this.dim,
        onDim = this.onDim,
        imageThumb = this.imageThumb,
        isDark = this.isDark
    )
}

private val LocalRandomColors = staticCompositionLocalOf<RandomPhotoColors> {
    error("No RandomPhotoColors provider")
}

fun debugColor(
    darkTheme: Boolean,
    colors: RandomPhotoColors,
    defaultColor: Color = Color.Unspecified
) = Colors(
    primary = colors.primary,
    primaryVariant = defaultColor,
    onPrimary = colors.onPrimary,
    background = colors.background,
    onBackground = defaultColor,
    surface = defaultColor,
    onSurface = defaultColor,
    secondary = defaultColor,
    secondaryVariant = defaultColor,
    onSecondary = defaultColor,
    error = defaultColor,
    onError = defaultColor,
    isLight = darkTheme.not()
)