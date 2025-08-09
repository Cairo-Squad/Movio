package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.AccountRepository
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import javax.inject.Inject

class GetRatedItemsUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend fun execute(page: Int): Pair<List<Movie>, List<Series>> {
        return accountRepository.getRatedItems(page)
    }
}
