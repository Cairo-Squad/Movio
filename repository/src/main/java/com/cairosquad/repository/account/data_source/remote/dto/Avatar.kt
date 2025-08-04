package com.cairosquad.repository.account.data_source.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Avatar(
    @SerialName("gravatar")
    val gravatar: Gravatar,
    @SerialName("tmdb")
    val tmdb: Tmdb
)