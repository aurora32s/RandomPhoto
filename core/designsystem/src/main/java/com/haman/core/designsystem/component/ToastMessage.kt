package com.haman.core.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun ToastMessage(
    modifier: Modifier = Modifier,
    @StringRes
    message: Int
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        ContentText(text = stringResource(id = message))
    }
}