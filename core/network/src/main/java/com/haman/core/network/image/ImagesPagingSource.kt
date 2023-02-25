package com.haman.core.network.image

import android.accounts.NetworkErrorException
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.haman.core.common.exception.ImageIOException
import com.haman.core.common.exception.ImageRequestNetworkException
import com.haman.core.common.exception.NoneImageResponseException
import com.haman.core.model.response.ImageResponse
import java.io.IOException

/**
 * 이미지 리스트 요청
 */
class ImagesPagingSource(
    private val imageApiService: ImageApiService
) : PagingSource<Int, ImageResponse>() {
    override fun getRefreshKey(state: PagingState<Int, ImageResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageResponse> {
        val page = params.key ?: INIT_KEY

        return try {
            val response = imageApiService.getImagesInfo(page = page, limit = LIMIT_PER_PAGE)
            if (response.isSuccessful && response.body() != null) {
                val images = response.body()!!
                LoadResult.Page(
                    data = images,
                    prevKey = if (page == INIT_KEY) null else page - 1,
                    nextKey = if (images.size < LIMIT_PER_PAGE) null else page + 1
                )
            } else {
                throw NoneImageResponseException()
            }
        } catch (exception: NetworkErrorException) {
            LoadResult.Error(ImageRequestNetworkException())
        } catch (exception: IOException) {
            LoadResult.Error(ImageIOException())
        } catch (exception: Exception) {
            LoadResult.Error(Exception(exception))
        }
    }

    companion object {
        const val INIT_KEY = 1
        const val LIMIT_PER_PAGE = 20
    }
}