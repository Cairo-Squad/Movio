package com.cairosquad.repository.account.data_source.local

import com.cairosquad.entity.Account
import com.cairosquad.repository.account.data_source.local.dto.AccountLocalDto

fun AccountLocalDto.toEntity() = Account(
    id = this.accountId,
    name = this.name,
    username = this.username,
    avatarPath = this.imagePath
)

fun Account.toCacheDto() = AccountLocalDto(
    accountId = id,
    name = name,
    username = username,
    imagePath = avatarPath
)