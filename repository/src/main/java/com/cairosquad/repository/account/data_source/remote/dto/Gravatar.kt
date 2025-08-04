package com.cairosquad.repository.account.data_source.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Gravatar(
    @SerialName("hash")
    val hash: String
)