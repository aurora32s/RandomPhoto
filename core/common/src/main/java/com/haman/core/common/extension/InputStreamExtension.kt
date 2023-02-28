package com.haman.core.common.extension

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import java.io.InputStream

fun InputStream?.decodeImage(): Bitmap? =
    BitmapFactory.decodeStream(this@decodeImage)

private fun calculateSamplingSize(options: Options, width: Int, height: Int): Int {
    val (imageHeight, imageWidth) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (imageHeight > height || imageWidth > width) {
        val halfHeight = imageHeight / 2
        val halfWidth = imageWidth / 2
        while (halfHeight / inSampleSize >= height && halfWidth / inSampleSize >= width) {
            inSampleSize++
        }
    }
    return inSampleSize
}