package com.haman.feature.home.model.image

import com.haman.core.domain.model.Image
import com.haman.feature.home.model.CellType

/**
 * feature.home 에 사용되는 Image model
 */
data class ImageUiModel(
    val id: String, // 이미지 id
    val type: CellType, // 아이템 type
    val author: String, // 이미지 창작자 명
    val width: Int, // 이미지 가로 길이
    val height: Int, // 이미지 세로 길이
    val imageUrl: String // 이미지 URL
)

fun Image.toUiModel() = ImageUiModel(
    id = id,
    type = CellType.GridImage,
    author = author,
    width = width,
    height = height,
    imageUrl = imageUrl
)