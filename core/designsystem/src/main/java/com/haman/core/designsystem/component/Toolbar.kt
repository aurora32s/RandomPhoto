package com.haman.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity

@Composable
fun CollapsingToolbar(
    modifier: Modifier,
    height: Float,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .height(with(LocalDensity.current) { height.toDp() }),
        content = content,
        contentAlignment = Alignment.Center
    )
}