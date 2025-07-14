package com.cairosquad.repository.search.data_source.remote.dto


import com.cairosquad.entity.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
) {
    fun toEntity(): Movie {
        return Movie(
            id = id?.toLong() ?: 0L,
            title = title ?: "",
            rating = voteAverage?.toFloat() ?: 0f,
            posterPath = posterPath ?: "",
        )

    }
}