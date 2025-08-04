package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.AccountRepository
import com.cairosquad.entity.Account
import javax.inject.Inject

class AccountUseCase @Inject constructor(
	private val accountRepository: AccountRepository
) {

	suspend fun getAccountDetails(): Account {
		return accountRepository.getAccountDetails()
	}
}