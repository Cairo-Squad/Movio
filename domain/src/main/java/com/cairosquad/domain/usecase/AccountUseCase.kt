package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.AccountRepository
import com.cairosquad.entity.Account
import com.cairosquad.entity.MediaList
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
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

    suspend fun addMovieToHistory(movieId: Long) {
        accountRepository.addMovieToHistory(movieId)
    }

    suspend fun addSeriesToHistory(seriesId: Long) {
        accountRepository.addSeriesToHistory(seriesId)
    }

    suspend fun getHistoryMovies(page: Int): List<Movie> {
        val asd = accountRepository.getHistoryMovies(page)
        return asd
    }

    suspend fun getHistorySeries(page: Int): List<Series> {
        return accountRepository.getHistorySeries(page)
    }
}