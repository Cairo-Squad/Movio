package com.cairosquad.repository.search.data_source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistRemoteDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("profile_path")
    val profilePath: String? = null,
    @SerialName("place_of_birth")
    val placeOfBirth: String? = null,
    @SerialName("birthday")
    val birthday: String? = null,
    @SerialName("biography")
    val biography: String? = null,
    @SerialName("known_for_department")
    val department: String? = null,
)