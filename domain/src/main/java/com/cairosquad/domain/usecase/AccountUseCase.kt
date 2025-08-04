package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.AccountRepository
import com.cairosquad.entity.Account
import com.cairosquad.entity.MediaList
import javax.inject.Inject

class AccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend fun getAccountDetails(): Account {
        return accountRepository.getAccountDetails()
    }

    suspend fun getSeriesLists(): List<MediaList> {
        return accountRepository.getSeriesLists(1)
    }

    suspend fun getMoviesLists(): List<MediaList> {
        return accountRepository.getMovieLists(1)
    }
}