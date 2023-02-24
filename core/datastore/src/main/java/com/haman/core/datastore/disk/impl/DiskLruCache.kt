package com.haman.core.datastore.disk.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.haman.core.datastore.disk.DiskCache
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject

class DiskLruCache @Inject constructor(
    @ApplicationContext
    private val context: Context
) : DiskCache {

    override suspend fun getBitmapFromDisk(id: String): Bitmap? {
        val file = File(context.cacheDir, "file_$id")
        return try {
            BitmapFactory.decodeStream(file.inputStream())
        } catch (exception: FileNotFoundException) {
            null
        }
    }

    override suspend fun putBitmapInDisk(id: String, bitmap: Bitmap) {
        val file = File.createTempFile("file_", id, context.cacheDir)
        file.outputStream().bufferedWriter().use {
            it.write(bitmap.toString())
        }
    }
}