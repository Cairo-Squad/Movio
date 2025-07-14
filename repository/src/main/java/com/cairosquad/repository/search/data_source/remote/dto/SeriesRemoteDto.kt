package com.cairosquad.repository.search.data_source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeriesRemoteDto(
    @SerialName("id")
    val id: Long? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
)