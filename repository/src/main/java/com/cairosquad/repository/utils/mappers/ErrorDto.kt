package com.cairosquad.repository.utils.mappers

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    @SerialName("status_message")
    val statusMessage: String? = null,
    @SerialName("status_code")
    val statusCode: Int? = null
)
