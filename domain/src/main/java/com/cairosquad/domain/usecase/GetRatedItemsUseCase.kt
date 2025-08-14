package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.AccountRepository
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import javax.inject.Inject

class GetRatedItemsUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend fun getRatedSeries(page: Int): List<Pair<Series, Double>> {
        return accountRepository.getRatedSeries(page)
    }

    suspend fun getRatedMovies(page: Int): List<Pair<Movie, Double>> {
        return accountRepository.getRatedMovies(page)
    }
}
