package com.haman.core.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haman.core.model.response.ImageResponse

@Entity(tableName = "image")
data class ImageEntity(
    @PrimaryKey
    val id: String, // 이미지 id
    val author: String, // 이미지 창작자 명
    val width: Int, // 이미지 가로 길이
    val height: Int, // 이미지 세로 길이
    val imageUrl: String // 이미지 URL
)

/**
 * network module image -> database module image
 */
fun ImageResponse.toEntity() = ImageEntity(
    id = id,
    author = author,
    width = width,
    height = height,
    imageUrl = imageUrl
)