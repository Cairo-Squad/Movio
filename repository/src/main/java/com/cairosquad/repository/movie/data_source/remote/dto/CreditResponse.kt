package com.cairosquad.repository.movie.data_source.remote.dto

import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreditResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("cast")
    val cast: List<ArtistRemoteDto?>? = null,
    // TODO: Add crew?
)
