package com.haman.core.datastore.internal.image

import android.content.Context
import android.graphics.Bitmap
import com.haman.core.common.di.IODispatcher
import com.haman.core.datastore.disk.impl.DiskLruCache
import com.haman.core.datastore.source.ImageCacheDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageCacheInDiskDataSource @Inject constructor(
    @ApplicationContext context: Context,
    @IODispatcher val ioDispatcher: CoroutineDispatcher
) : ImageCacheDataSource {

    private lateinit var diskLruCache: DiskLruCache

    init {
        CoroutineScope(ioDispatcher).launch {
            diskLruCache = DiskLruCache.open(context.filesDir, 1024 * 1024 * 10)
        }
    }

    override suspend fun getImage(id: String, reqWidth: Int): Bitmap? {
        return diskLruCache.getBitmapFromDisk(id, reqWidth)
    }

    override suspend fun addImage(id: String, width: Int, bitmap: Bitmap) {
        diskLruCache.putBitmapInDisk(id, bitmap)
    }
}