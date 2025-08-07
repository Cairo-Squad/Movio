package com.cairosquad.repository.account.data_source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaListResponse(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<MediaListDto>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int,
)