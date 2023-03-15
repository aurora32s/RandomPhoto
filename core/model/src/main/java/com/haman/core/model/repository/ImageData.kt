package com.haman.core.model.repository

import com.haman.core.model.entity.ImageEntity
import com.haman.core.model.response.ImageResponse

/**
 * data module 의 이미지 정보 Model
 */
data class ImageData(
    val id: String, // 이미지 id
    val author: String, // 이미지 창작자 명
    val width: Int, // 이미지 가로 길이
    val height: Int, // 이미지 세로 길이
    val imageUrl: String // 이미지 URL
)

/**
 * database module image -> data module image
 */
fun ImageEntity.toData() = ImageData(
    id = id,
    author = author,
    width = width,
    height = height,
    imageUrl = imageUrl
)

/**
 * network module image -> data module image
 */
fun ImageResponse.toData() = ImageData(
    id = id,
    author = author,
    width = width,
    height = height,
    imageUrl = imageUrl
)