package com.cairosquad.repository.search.data_source.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResultDto<T>(
    @SerialName("page")
    val page: Int? = null,
    @SerialName("results")
    val results: List<T?>? = null,
    @SerialName("total_pages")
    val totalPages: Int? = null,
    @SerialName("total_results")
    val totalResults: Int? = null
)