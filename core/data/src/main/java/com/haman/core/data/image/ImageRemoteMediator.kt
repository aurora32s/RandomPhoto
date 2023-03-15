package com.haman.core.data.image

import androidx.paging.*
import androidx.room.withTransaction
import com.haman.core.common.exception.NoneImageResponseException
import com.haman.core.database.RandomPhotoDatabase
import com.haman.core.model.entity.ImageEntity
import com.haman.core.model.entity.RemoteKeys
import com.haman.core.model.entity.toEntity
import com.haman.core.network.source.ImageDataSource

@OptIn(ExperimentalPagingApi::class)
class ImageRemoteMediator(
    private val imageDataSource: ImageDataSource,
    private val randomDatabase: RandomPhotoDatabase
) : RemoteMediator<Int, ImageEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INIT_KEY
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        return try {
            val response = imageDataSource.getImagesInfo(page = page, limit = state.config.pageSize)

            if (response.isSuccess) {
                val images = response.getOrDefault(emptyList())
                val endOfPaginationReached = images.size < state.config.pageSize

                // remote key 저장
                randomDatabase.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        randomDatabase.getRemoteKeysDao().deleteAll()
                        randomDatabase.getImageDao().deleteAll()
                    }

                    val prevKey = if (page == INIT_KEY) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    val keys = images.map { RemoteKeys(it.id, prevKey, nextKey) }
                    randomDatabase.getRemoteKeysDao().insertAll(keys)
                    randomDatabase.getImageDao().insertAll(images.map { it.toEntity() })
                }

                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } else {
                throw response.exceptionOrNull() ?: NoneImageResponseException()
            }
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ImageEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { imageId ->
                randomDatabase.getRemoteKeysDao().remoteKeysImageId(imageId)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ImageEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let {
                randomDatabase.getRemoteKeysDao().remoteKeysImageId(it.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ImageEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let {
                randomDatabase.getRemoteKeysDao().remoteKeysImageId(it.id)
            }
    }

    companion object {
        const val INIT_KEY = 1
        const val LIMIT_PER_PAGE = 100
    }
}