package com.cairosquad.repository.login.data_source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionIdResponse(
    @SerialName("session_id")
    val sessionId: String? = null
) {
    fun toEntity(): String {
        return sessionId ?: ""
    }
}
