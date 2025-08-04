package com.cairosquad.repository.account.data_source.remote

import com.cairosquad.entity.Account
import com.cairosquad.repository.account.data_source.remote.dto.AccountDto

internal fun AccountDto.toEntity() = Account(
	id = id,
	name = name,
	username = username,
	avatarPath = avatar.tmdb.avatarPath ?: ""
)