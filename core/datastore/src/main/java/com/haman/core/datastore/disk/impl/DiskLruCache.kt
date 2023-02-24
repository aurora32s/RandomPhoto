package com.haman.core.datastore.disk.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.haman.core.datastore.disk.DiskCache
import java.io.File
import javax.inject.Inject

class DiskLruCache @Inject constructor(
    private val context: Context
) : DiskCache {

    override suspend fun getBitmapFromDisk(id: String): Bitmap? {
        val input = File(context.cacheDir, "file_$id").inputStream()
        return BitmapFactory.decodeStream(input)
    }

    override suspend fun putBitmapInDisk(id: String, bitmap: Bitmap) {
        val file = File.createTempFile("file_$id", null, context.cacheDir)
        file.outputStream().bufferedWriter().use {
            it.write(bitmap.toString())
        }
    }
}