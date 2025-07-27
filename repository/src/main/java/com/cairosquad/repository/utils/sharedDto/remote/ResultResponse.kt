package com.cairosquad.repository.utils.sharedDto.remote


import com.cairosquad.repository.movie.data_source.remote.dto.GenreDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultResponse<T>(
    @SerialName("page")
    val page: Int? = null,
    @SerialName("results")
    val results: List<T?>? = null,
    @SerialName("total_pages")
    val totalPages: Int? = null,
    @SerialName("total_results")
    val totalResults: Int? = null
)

@Serializable
data class GenreResponse(
    @SerialName("genres")
    val genres: List<GenreDto>? = null,
)