package com.haman.core.designsystem.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

val Orange800 = Color(0xFFEF6C00)
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Gray900 = Color(0xFF212121)
val Gray700 = Color(0xFF616161)
val Gray600 = Color(0xFF585858)
val Gray200 = Color(0xFFEEEEEE)

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

internal val LocalRandomColors = staticCompositionLocalOf<RandomPhotoColors> {
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