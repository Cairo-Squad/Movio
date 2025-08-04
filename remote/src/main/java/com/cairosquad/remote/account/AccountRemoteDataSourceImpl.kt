package com.cairosquad.remote.account

import com.cairosquad.repository.account.data_source.remote.AccountRemoteDataSource

class AccountRemoteDataSourceImpl(
	private val apiService: AccountApiService
): AccountRemoteDataSource {
}