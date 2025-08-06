package com.cairosquad.repository.account.data_source.remote

import com.cairosquad.entity.Account
import com.cairosquad.entity.MediaList
import com.cairosquad.repository.account.data_source.remote.dto.MediaListDto
import com.cairosquad.repository.account.data_source.remote.dto.acount.AccountDto

internal fun AccountDto.toEntity() = Account(
    id = id ?: 0,
    name = name ?: "",
    username = username ?: "",
    avatarPath = avatar?.avatarPath?.avatarPath ?: ""
)

internal fun MediaListDto.toEntity() = MediaList(
    id = id,
    name = name,
    mediaCount = mediaCount
)