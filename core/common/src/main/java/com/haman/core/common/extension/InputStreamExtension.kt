package com.haman.core.common.extension

import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

suspend fun InputStream?.decodeImage() = withContext(Dispatchers.Default) {
    BitmapFactory.decodeStream(this@decodeImage)
}