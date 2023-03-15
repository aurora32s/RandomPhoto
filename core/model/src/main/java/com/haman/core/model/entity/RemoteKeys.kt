package com.haman.core.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remove_keys")
data class RemoteKeys(
    @PrimaryKey
    val id: String, // 이미지 id
    val prevKey: Int?,
    val nextKey: Int?
)
