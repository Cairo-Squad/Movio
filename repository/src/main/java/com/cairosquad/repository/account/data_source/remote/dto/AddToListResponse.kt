package com.cairosquad.repository.account.data_source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddToListResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("status_code")
    val statusCode: Long,
)
