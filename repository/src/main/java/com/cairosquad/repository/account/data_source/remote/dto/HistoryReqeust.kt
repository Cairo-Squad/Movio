package com.cairosquad.repository.account.data_source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoryRequest(
    @SerialName("media_type")
    val mediaType: String,
    @SerialName("media_id")
    val mediaId: Long,
    @SerialName("watchlist")
    val watchlist: Boolean
)
