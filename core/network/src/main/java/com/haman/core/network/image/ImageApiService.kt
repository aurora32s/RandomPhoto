package com.haman.core.network.image

import com.haman.core.model.response.ImageResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 이미지 관련 api 관리
 */
interface ImageApiService {
    /**
     * 서버로부터 이미지 자체를 요청
     * @param id 이미지 id
     * @param width 이미지 가로 길이
     * @param height 이미지 세로 길이
     */
    @GET("id/{id}/{width}/{height}")
    suspend fun getImage(
        @Path("id") id: String,
        @Path("width") width: Int,
        @Path("height") height: Int
    ): Response<ResponseBody>

    /**
     * 이미지 리스트 요청
     * @param page 페이지 번호
     * @param limit 페이지 당 이미지 개수
     */
    @GET("v2/list")
    suspend fun getImagesInfo(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<List<ImageResponse>>

    /**
     * 특정 이미지 정보 요청
     * @param id 이미지 id
     */
    @GET("id/{id}/info")
    suspend fun getImageInfo(
        @Path("id") id: String
    ): Response<ImageResponse>

    /**
     * 랟덤 이미지 정보 요청
     * @param seed 랜덤 정보에 사용될 seed 정보
     */
    @GET("seed/{seed}/info")
    suspend fun getRandomImageInfo(
        @Path("seed") seed: String
    ): Response<ImageResponse>
}