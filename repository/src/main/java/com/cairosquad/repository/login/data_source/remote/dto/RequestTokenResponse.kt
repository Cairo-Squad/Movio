package com.cairosquad.repository.login.data_source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestTokenResponse(
    @SerialName("request_token")
    val requestToken: String? = null
) {
    fun toEntity(): String {
        return requestToken ?: ""
    }
}