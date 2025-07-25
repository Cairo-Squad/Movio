package com.cairosquad.repository.search.data_source.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieRemoteDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("genre_ids")
    val genreIds: List<Int>? = null,
)