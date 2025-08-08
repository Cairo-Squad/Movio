package com.cairosquad.repository.account.data_source.local

import com.cairosquad.repository.account.data_source.local.dto.AccountLocalDto

interface AccountLocalDataSource {

    suspend fun setAccount(account: AccountLocalDto)

    suspend fun getAccount(): List<AccountLocalDto>

    suspend fun getAccountId(): Long?

    suspend fun removeAccount()
}