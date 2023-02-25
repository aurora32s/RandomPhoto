package com.haman.core.designsystem.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import com.haman.core.designsystem.icon.DaangnIcons
import com.haman.core.designsystem.theme.Black
import com.haman.core.designsystem.theme.Gray200
import com.haman.core.designsystem.theme.Gray700
import com.haman.core.designsystem.theme.Gray900

@Composable
fun AsyncImage(
    modifier: Modifier = Modifier,
    width: Int = 200,
    height: Int = 300,
    id: String,
    load: suspend (String, Int, Int) -> Bitmap?,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    val bitmap = produceState<LoadImageState>(initialValue = LoadImageState.Loading) {
        val result = load(id, width, height)
        value = result?.let { LoadImageState.Success(it) } ?: LoadImageState.Error
    }
    val backgroundColor = remember {
        derivedStateOf {
            when (bitmap.value) {
                LoadImageState.Error -> if (isDarkTheme) Black else Gray900
                else -> if (isDarkTheme) Gray700 else Gray200
            }
        }
    }

    Box(
        modifier = modifier.background(backgroundColor.value)
    ) {
        when (val result = bitmap.value) {
            is LoadImageState.Success -> Image(
                bitmap = result.bitmap.asImageBitmap(),
                contentDescription = "Image's id is $id"
            )
            is LoadImageState.Error -> Image(
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