package com.cairosquad.remote.account

import android.util.Log
import com.cairosquad.repository.account.data_source.remote.AccountRemoteDataSource
import com.cairosquad.repository.account.data_source.remote.dto.AccountDto
import javax.inject.Inject

class AccountRemoteDataSourceImpl @Inject constructor(
	private val apiService: AccountApiService
): AccountRemoteDataSource {
	override suspend fun getAccountDetails(): AccountDto {
		val details =  apiService.getAccountDetails()
		Log.d("Account Details", "getAccountDetails: $details")
		return  details
	}
}