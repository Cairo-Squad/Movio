package com.cairosquad.repository.series.data_source.remote.dto


import com.cairosquad.entity.Series
import com.cairosquad.repository.movie.data_source.remote.dto.GenreDto
import com.cairosquad.repository.utils.TimeUtils
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeriesDetailsRemoteDto(
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("first_air_date")
    val firstAirDate: String? = null,
    @SerialName("genres")
    val genres: List<GenreDto?>? = null,
    @SerialName("id")
    val id: Long? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("number_of_episodes")
    val numberOfEpisodes: Int? = null,
    @SerialName("number_of_seasons")
    val numberOfSeasons: Int? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("type")
    val type: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
    @SerialName("vote_count")
    val voteCount: Int? = null
) {
    fun toEntity(trailerPath: String) = Series(
        id = id ?: 0,
        title = name.orEmpty(),
        rating = voteAverage?.toFloat()?.times(.5f) ?: 0f,
        posterPath = posterPath.orEmpty(),
        genres = genres?.mapNotNull { it?.toEntity() } ?: emptyList(),
        overview = overview.orEmpty(),
        releaseDate = firstAirDate?.let { TimeUtils.dateToLong(it) } ?: 0L,
        seasonsCount = numberOfSeasons ?: 1,
        trailerPath = trailerPath
    )
}