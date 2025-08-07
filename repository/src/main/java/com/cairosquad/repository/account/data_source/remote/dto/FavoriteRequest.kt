package com.cairosquad.repository.account.data_source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteRequest(
	@SerialName("media_type")
	val type: String,
	@SerialName("media_id")
	val id: Long,
	@SerialName("favorite")
	val favorite: Boolean
)
