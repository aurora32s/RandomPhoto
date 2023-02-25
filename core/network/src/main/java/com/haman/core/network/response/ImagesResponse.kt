package com.haman.core.network.response

import kotlinx.serialization.Serializable

/**
 * 이미지 리스트 요청 Response
 */
@Serializable
data class ImagesResponse(
    val images: List<ImageResponse>
)
