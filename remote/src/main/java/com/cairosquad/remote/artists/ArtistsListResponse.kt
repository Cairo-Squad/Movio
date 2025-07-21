package com.cairosquad.remote.artists

import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto
import kotlinx.serialization.Serializable

@Serializable
data class MoviesListResponse(
    val cast: List<MovieRemoteDto> = emptyList()
)

@Serializable
data class SeriesListResponse(
    val cast: List<SeriesRemoteDto> = emptyList()
)