package com.cairosquad.repository.series.data_source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeasonResponse(
    @SerialName("episodes")
    val episodes: List<EpisodeRemoteDto>? = null,
    @SerialName("id")
    val id: Long
)