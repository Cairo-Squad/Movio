package com.cairosquad.remote.artists.response

import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import kotlinx.serialization.Serializable

@Serializable
data class MoviesListResponse(
    val cast: List<MovieRemoteDto>
)