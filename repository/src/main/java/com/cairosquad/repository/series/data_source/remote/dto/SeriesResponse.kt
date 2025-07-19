package com.cairosquad.repository.series.data_source.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeriesResponse(
    @SerialName("name")
    val name: String?,
    @SerialName("number_of_episodes")
    val numberOfEpisodes: Int?,
    @SerialName("number_of_seasons")
    val numberOfSeasons: Int?,
    @SerialName("seasons")
    val seasons: List<SeasonRemoteDto?>?
)