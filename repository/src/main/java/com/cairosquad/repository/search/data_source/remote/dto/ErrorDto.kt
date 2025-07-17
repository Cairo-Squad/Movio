package com.cairosquad.repository.search.data_source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    @SerialName("status_message")
    val statusMessage: String?,
    @SerialName("success")
    val success: Boolean?
)