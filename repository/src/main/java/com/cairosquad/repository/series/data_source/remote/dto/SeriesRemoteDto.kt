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
    @SerialName("genre_ids")
    val genreIds: List<Long>? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("first_air_date")
    val releaseDate: String? = null,
    @SerialName("rating")
    val userRating: Double? = null
)