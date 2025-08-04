package com.cairosquad.repository.account.data_source.remote

import com.cairosquad.repository.account.data_source.remote.dto.AccountDto

interface AccountRemoteDataSource {

	suspend fun getAccountDetails(): AccountDto
}