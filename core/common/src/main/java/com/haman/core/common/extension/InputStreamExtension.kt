package com.haman.core.common.extension

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Bitmap Sampling
 * @param width sampling width
 * @param height sampling height
 */
suspend fun ByteArray?.decodeImage(width: Int, height: Int): Bitmap? =
    withContext(Dispatchers.Default) {
        this@decodeImage ?: return@withContext null
        val option = BitmapFactory.Options()
        option.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(this@decodeImage, 0, this@decodeImage.size, option)
        option.inSampleSize = calculateSamplingSize(option, width, height)
        option.inJustDecodeBounds = false
        return@withContext BitmapFactory.decodeByteArray(
            this@decodeImage,
            0,
            this@decodeImage.size,
            option
        )
    }

private fun calculateSamplingSize(options: BitmapFactory.Options, width: Int, height: Int): Int {
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