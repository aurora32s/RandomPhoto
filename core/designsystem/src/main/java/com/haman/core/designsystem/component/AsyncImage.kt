package com.haman.core.designsystem.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.haman.core.designsystem.icon.DaangnIcons
import com.haman.core.designsystem.theme.Gray200
import com.haman.core.designsystem.theme.Gray700

@Composable
fun AsyncImage(
    modifier: Modifier = Modifier,
    width: Int = 200,
    height: Int = 300,
    id: String,
    cornerRadius: Float = 4f,
    load: suspend (String, Int, Int) -> Bitmap?,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    val bitmap = produceState<LoadImageState>(initialValue = LoadImageState.Loading) {
        val result = load(id, width, height)
        value = result?.let { LoadImageState.Success(it) } ?: LoadImageState.Error
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius.dp))
            .background(if (isDarkTheme) Gray700 else Gray200),
        contentAlignment = Alignment.Center
    ) {
        when (val result = bitmap.value) {
            is LoadImageState.Success -> Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(cornerRadius.dp)),
                bitmap = result.bitmap.asImageBitmap(),
                contentDescription = "Image's id is $id",
                contentScale = ContentScale.FillWidth
            )
            is LoadImageState.Error -> Image(
                modifier = Modifier.size(50.dp),
                painter = painterResource(id = DaangnIcons.logo),
                contentDescription = "Fail to load $id Image"
            )
            else -> {}
        }
    }
}

sealed class LoadImageState {
    object Loading : LoadImageState()

    data class Success(
        val bitmap: Bitmap
    ) : LoadImageState()

    object Error : LoadImageState()
}