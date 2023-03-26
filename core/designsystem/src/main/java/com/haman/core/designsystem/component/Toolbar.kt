package com.haman.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity

@Composable
inline fun CollapsingToolbar(
    modifier: Modifier = Modifier,
    heightProvider: () -> Float,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .height(with(LocalDensity.current) { heightProvider().toDp() })
        ,
        content = content,
        contentAlignment = Alignment.Center
    )
}
