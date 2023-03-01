package com.haman.core.datastore.disk

import android.graphics.Bitmap

interface DiskCache {
    /**
     * 이미지 id 를 이용해 Disk 에서 이미지(Bitmap) 요청
     * @param id 이미지 Id
     * @return Bitmap 이미지(Bitmap), Disk Cache 에 없을 경우 null 반환
     */
    suspend fun getBitmapFromDisk(id: String, reqWidth: Int, reqHeight: Int): Bitmap?

    /**
     * 이미지를 Disk 에 Caching
     * @param id 이미지 Id
     * @param bitmap 이미지
     */
    suspend fun putBitmapInDisk(id: String, bitmap: Bitmap)
}