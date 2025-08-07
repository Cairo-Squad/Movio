package com.cairosquad.repository.account.data_source.remote.dto.acount


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvatarPath(
    @SerialName("avatar_path")
    val avatarPath: String?
)