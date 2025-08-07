package com.cairosquad.repository.account.data_source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateListRequest(
    @SerialName("name")
    val name: String,
    @SerialName("iso_639_1")
    val language: String,
    @SerialName("description")
    val description: String = "",
)
