package com.cairosquad.repository.series.data_source.remote.dto


import com.cairosquad.entity.Episode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeRemoteDto(
    @SerialName("air_date")
    val airDate: String? = null,
    @SerialName("episode_number")
    val episodeNumber: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("id")
    val id: Long? = null,
    @SerialName("runtime")
    val runtime: Int? = null,
    @SerialName("season_number")
    val seasonNumber: Int? = null,
    @SerialName("still_path")
    val stillPath: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
    @SerialName("show_id")
    val seriesId: Long
) {
    fun toEntity(): Episode {
        return Episode(
            id = id ?: 0,
            episodeNumber = episodeNumber ?: 0,
            photoPath = stillPath.orEmpty(),
            episodeName = name.orEmpty(),
            runtimeMinutes = runtime ?: 0,
            rating = voteAverage?.toFloat()?.times(0.5f) ?: 0f,
            seasonNumber = seasonNumber ?: 0,
            seriesId = seriesId,
        )
    }
}