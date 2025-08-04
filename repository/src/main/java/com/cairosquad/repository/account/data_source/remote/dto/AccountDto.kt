package com.cairosquad.repository.account.data_source.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountDto(
	@SerialName("avatar")
	val avatar: Avatar? = null,
	@SerialName("id")
	val id: Long? = null,
	@SerialName("name")
	val name: String? = null,
	@SerialName("username")
	val username: String? = null
)