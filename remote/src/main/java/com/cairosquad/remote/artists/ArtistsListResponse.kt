package com.cairosquad.remote.artists

import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesListResponse(
    @SerialName("cast")
    val movies: List<MovieRemoteDto> = emptyList()
)

@Serializable
data class SeriesListResponse(
    @SerialName("cast")
    val series: List<SeriesRemoteDto> = emptyList()
)