package com.haman.core.designsystem.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.haman.core.designsystem.R

val font = FontFamily(
    Font(R.font.font_juna)
)

// Set of Material typography styles to start with
val Typography = Typography(
    h5 = TextStyle(
        fontFamily = font,
        fontSize = 32.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = font,
        fontSize = 24.sp
    ),
    caption = TextStyle(
        fontFamily = font,
        fontSize = 16.sp
    )
)