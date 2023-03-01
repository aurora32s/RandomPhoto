package com.haman.core.data.fake

import com.haman.core.model.response.ImageResponse
import com.haman.core.network.image.ImageApiService
import okhttp3.ResponseBody
import retrofit2.Response

class FakeImageApiService : ImageApiService {
    override suspend fun getImage(id: String, width: Int, height: Int): Response<ResponseBody> {
        return Response.error(0, null)
    }
    override suspend fun getImagesInfo(page: Int, limit: Int): Response<List<ImageResponse>> {
        return Response.error(0, null)
    }
    override suspend fun getImageInfo(id: String): Response<ImageResponse> {
        return Response.error(0, null)
    }
    override suspend fun getRandomImageInfo(seed: String): Response<ImageResponse> {
        return Response.error(0, null)
    }
}