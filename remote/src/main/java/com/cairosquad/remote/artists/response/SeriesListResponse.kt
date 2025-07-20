package com.cairosquad.remote.artists.response

import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto
import kotlinx.serialization.Serializable

@Serializable
data class SeriesListResponse(
    val cast: List<SeriesRemoteDto>
)