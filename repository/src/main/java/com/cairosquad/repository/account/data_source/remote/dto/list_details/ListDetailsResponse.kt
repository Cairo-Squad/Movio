package com.cairosquad.repository.account.data_source.remote.dto.list_details


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListDetailsResponse(
    @SerialName("created_by")
    val createdBy: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("favorite_count")
    val favoriteCount: Int? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("iso_639_1")
    val iso6391: String? = null,
    @SerialName("item_count")
    val itemCount: Int? = null,
    @SerialName("items")
    val items: List<ListDetailsItem>? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("page")
    val page: Int? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("total_pages")
    val totalPages: Int? = null,
    @SerialName("total_results")
    val totalResults: Int? = null
)