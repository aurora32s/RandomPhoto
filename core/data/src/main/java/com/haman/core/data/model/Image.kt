package com.haman.core.data.model

import com.haman.core.network.response.ImageResponse

/**
 * 이미지 정보 Model
 */
data class Image(
    val id: String, // 이미지 id
    val author: String, // 이미지 창작자 명
    val width: Int, // 이미지 가로 길이
    val height: Int, // 이미지 세로 길이
    val imageUrl: String // 이미지 URL
)

fun ImageResponse.toModel() = Image(
    id = id,
    author = author,
    width = width,
    height = height,
    imageUrl = imageUrl
)