package com.cairosquad.repository.series.data_source.remote.dto


import com.cairosquad.entity.Season
import com.cairosquad.repository.utils.TimeUtils
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeasonRemoteDto(
    @SerialName("air_date")
    val airDate: String? = null,
    @SerialName("episode_count")
    val episodeCount: Int? = null,
    @SerialName("id")
    val id: Long? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("season_number")
    val seasonNumber: Int? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null
) {
    fun toEntity(seriesId: Long): Season {
        return Season(
            seasonNumber = seasonNumber ?: 0,
            seasonName = name.orEmpty(),
            seriesId = seriesId,
            episodesCount = episodeCount ?: 0,
            rating = voteAverage?.toFloat()?.times(0.5f) ?: 0f,
            posterPath = posterPath.orEmpty(),
            overview = overview.orEmpty(),
            airDate = TimeUtils.dateToLong(airDate ?: "")
        )
    }
}