package com.cairosquad.repository.movie.data_source.remote.dto


import com.cairosquad.entity.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsRemoteDto(
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("genres")
    val genres: List<GenreDto?>? = null,
    @SerialName("id")
    val id: Long,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("runtime")
    val runtime: Int? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("video")
    val video: Boolean? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
    @SerialName("vote_count")
    val voteCount: Int? = null
) {
    fun toEntity() = Movie(
        id = id,
        title = title.orEmpty(),
        rating = voteAverage?.toFloat() ?: 0f,
        posterPath = posterPath.orEmpty(),
        genres = genres?.mapNotNull { it?.toEntity() } ?: emptyList(),
        overview = overview.orEmpty(),
        releaseDate = releaseDate,
        runtimeMinutes = runtime ?: 0,
    )
}