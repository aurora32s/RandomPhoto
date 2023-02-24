package com.haman.core.ui.item

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun AsyncImage(
    width: Int = 200,
    height: Int = 300,
    id: String,
    load: suspend (String, Int, Int) -> Bitmap?
) {
    val bitmap = produceState<LoadImageState>(initialValue = LoadImageState.Loading) {
        val result = load(id, width, height)
        value = result?.let { LoadImageState.Success(it) } ?: LoadImageState.Error
    }

    when (val result = bitmap.value) {
        is LoadImageState.Success -> Image(
            bitmap = result.bitmap.asImageBitmap(),
            contentDescription = ""
        )
        else -> {}
    }
}

sealed class LoadImageState {
    object Loading : LoadImageState()

    data class Success(
        val bitmap: Bitmap
    ) : LoadImageState()

    object Error : LoadImageState()
}