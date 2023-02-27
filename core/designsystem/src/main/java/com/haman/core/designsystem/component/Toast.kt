package com.haman.core.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.haman.core.common.state.ToastPosition
import kotlinx.coroutines.delay

@Composable
fun BoxScope.Toast(
    type: ToastPosition,
    @StringRes
    message: Int
) {
    val visible = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = message) {
        visible.value = true
        delay(3000L)
        visible.value = false
    }

    if (visible.value) {
        Row(
            modifier = Modifier
                .align(
                    when (type) {
                        ToastPosition.BOTTOM -> Alignment.BottomCenter
                        ToastPosition.MIDDLE -> Alignment.Center
                    }
                )
                .padding(16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colors.surface.copy(alpha = 0.7f))
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            ContentText(
                text = stringResource(id = message),
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}