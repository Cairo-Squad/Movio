package com.cairosquad.repository.utils.sharedDto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    @SerialName("status_message")
    val statusMessage: String?,
    @SerialName("success")
    val success: Boolean?
)