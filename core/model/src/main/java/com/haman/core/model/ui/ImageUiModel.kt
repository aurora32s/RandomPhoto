package com.haman.core.model.ui

import com.haman.core.model.domain.Image

/**
 * feature module 에서 사용되는 Image model
 */
data class ImageUiModel(
    override val id: String, // 이미지 id
    override val type: CellType, // 아이템 type
    val author: String, // 이미지 창작자 명
    val width: Int, // 이미지 가로 길이
    val height: Int, // 이미지 세로 길이
    val imageUrl: String // 이미지 URL
) : UiModel

/**
 * domain module image -> feature module image
 */
fun Image.toUiModel() = ImageUiModel(
    id = id,
    type = CellType.GridImage,
    author = author,
    width = width,
    height = height,
    imageUrl = imageUrl
)