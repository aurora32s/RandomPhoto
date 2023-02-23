package com.haman.core.ui.item

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun AsyncImage(
    id: String,
    width: Int = 200,
    height: Int = 300,
    requestImage: (String, Int, Int) -> Bitmap?
) {
    val bitmap = produceState<LoadImageState>(initialValue = LoadImageState.Loading) {
        val result = requestImage(id, width, height)
        value = result?.let { LoadImageState.Success(it) } ?: LoadImageState.Error
    }
    val resource = remember {
        derivedStateOf {
            when (val result = bitmap.value) {
                LoadImageState.Loading -> 1
                is LoadImageState.Success -> result.bitmap.asImageBitmap()
                LoadImageState.Error -> 3
            }
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