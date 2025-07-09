package com.cairosquad.repository.search.dataSource.remote.search.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResultDto<T>(
    @SerialName("page")
    val page: Int?,
    @SerialName("results")
    val results: List<T?>?,
    @SerialName("total_pages")
    val totalPages: Int?,
    @SerialName("total_results")
    val totalResults: Int?
)