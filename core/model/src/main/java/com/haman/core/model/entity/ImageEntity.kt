package com.haman.core.model.entity

import com.haman.core.model.response.ImageResponse

/**
 * data module 의 이미지 정보 Model
 */
data class ImageEntity(
    val id: String, // 이미지 id
    val author: String, // 이미지 창작자 명
    val width: Int, // 이미지 가로 길이
    val height: Int, // 이미지 세로 길이
    val imageUrl: String // 이미지 URL
)

/**
 * network module image -> data module image
 */
fun ImageResponse.toEntity() = ImageEntity(
    id = id,
    author = author,
    width = width,
    height = height,
    imageUrl = imageUrl
)