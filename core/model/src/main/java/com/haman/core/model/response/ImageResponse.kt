package com.haman.core.model.response

import kotlinx.serialization.Serializable

/**
 * network module 의 이미지 정보 Response
 */
@Serializable
data class ImageResponse(
    val id: String, // 이미지 id
    val author: String, // 이미지 창작자 명
    val width: Int, // 이미지 가로 길이
    val height: Int, // 이미지 세로 길이
    val imageUrl: String // 이미지 URL
)
