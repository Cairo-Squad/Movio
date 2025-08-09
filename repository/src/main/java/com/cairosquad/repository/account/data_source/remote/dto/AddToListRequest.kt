package com.cairosquad.repository.account.data_source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddToListRequest(
    @SerialName("media_id")
    val mediaId: Long
)
