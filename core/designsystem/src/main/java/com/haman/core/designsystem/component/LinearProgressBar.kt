package com.haman.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LinearProgressBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.background,
    color: Color = MaterialTheme.colors.primary
) {
    LinearProgressIndicator(
        modifier = modifier
            .fillMaxWidth()
            .height(3.dp),
        backgroundColor = backgroundColor,
        color = color
    )
}