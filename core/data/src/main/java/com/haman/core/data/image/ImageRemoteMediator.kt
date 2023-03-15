package com.haman.core.data.image

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.haman.core.database.RandomPhotoDatabase
import com.haman.core.model.entity.ImageEntity
import com.haman.core.network.image.ImageApiService

@OptIn(ExperimentalPagingApi::class)
class ImageRemoteMediator(
    private val imageApiService: ImageApiService,
    private val randomDatabase: RandomPhotoDatabase
) : RemoteMediator<Int, ImageEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageEntity>
    ): MediatorResult {
        TODO("Not yet implemented")
    }
}