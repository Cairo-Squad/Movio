package com.cairosquad.repository.movie.data_source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class VideoResponse(
    val results: List<VideoResult>? = null
) {
    fun getVideoKey(): String? {
        return results?.firstOrNull()?.key
    }
}

@Serializable
data class VideoResult(
    val key: String? = null,
)