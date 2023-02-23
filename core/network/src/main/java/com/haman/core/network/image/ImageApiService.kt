package com.haman.core.network.image

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 이미지 관련 api 관리
 */
private interface ImageApiService {
    /**
     * 서버로부터 이미지 자체를 요청
     * @path id 이미지 id
     */
    @GET("id/{id}")
    fun getImage(@Path("id") id: String): Response<String>
}