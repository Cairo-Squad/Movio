package com.cairosquad.repository.series.data_source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeriesRemoteDto(
    @SerialName("id")
    val id: Long? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("vote_average")
    val voteAverage: Float? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
)