package com.haman.core.designsystem.component

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CustomText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle,
    align: TextAlign = TextAlign.Center,
    bold: Boolean = false,
    color: Color = Color.Unspecified
) {
    Text(
        modifier = modifier,
        text = text,
        style = style,
        textAlign = align,
        fontWeight = if (bold) FontWeight.Bold else null,
        color = color
    )
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    text: String,
    align: TextAlign = TextAlign.Start,
    color: Color = Color.Unspecified
) {
    CustomText(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.h5,
        align = align,
        color = color
    )
}

@Composable
fun SubTitle(
    modifier: Modifier = Modifier,
    text: String,
    align: TextAlign = TextAlign.Center,
    color: Color = Color.Unspecified
) {
    CustomText(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.subtitle1,
        align = align,
        color = color
    )
}

@Composable
fun ContentText(
    modifier: Modifier = Modifier,
    text: String,
    align: TextAlign = TextAlign.Start,
    color: Color = Color.Unspecified
) {
    CustomText(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.caption,
        align = align,
        color = color
    )
}