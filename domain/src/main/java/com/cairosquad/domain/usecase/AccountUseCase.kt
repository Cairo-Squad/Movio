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

    suspend fun removeAccountDetails() {
        accountRepository.removeAccountDetails()
    }

    suspend fun getSeriesLists(page: Int): List<MediaList> {
        return accountRepository.getSeriesLists(page)
    }

    suspend fun getMoviesLists(page: Int): List<MediaList> {
        return accountRepository.getMovieLists(page)
    }

    suspend fun getFavoriteMovies(page: Int): List<Movie> {
        return accountRepository.getFavoriteMovies(page)
    }

    suspend fun getFavoriteSeries(page: Int): List<Series> {
        return accountRepository.getFavoriteSeries(page)
    }

    suspend fun addMovieToFavorite(movieId: Long) {
        accountRepository.addMovieToFavorite(movieId)
    }

    suspend fun addSeriesToFavorite(seriesId: Long) {
        accountRepository.addSeriesToFavorite(seriesId)
    }

    suspend fun removeMovieFromFavorite(movieId: Long) {
        accountRepository.removeMovieFromFavorite(movieId)
    }

    suspend fun removeSeriesFromFavorite(seriesId: Long) {
        accountRepository.removeSeriesFromFavorite(seriesId)
    }

    suspend fun addMovieToHistory(movieId: Long) {
        accountRepository.addMovieToHistory(movieId)
    }

    suspend fun removeMovieFromHistory(movieId: Long) {
        accountRepository.removeMovieFromHistory(movieId)
    }

    suspend fun getHistoryMovies(page: Int): List<Movie> {
        return accountRepository.getHistoryMovies(page)
    }

    suspend fun addSeriesToHistory(seriesId: Long) {
        accountRepository.addSeriesToHistory(seriesId)
    }

    suspend fun removeSeriesFromHistory(seriesId: Long) {
        accountRepository.removeSeriesFromHistory(seriesId)
    }

    suspend fun getHistorySeries(page: Int): List<Series> {
        return accountRepository.getHistorySeries(page)
    }

    suspend fun getMoviesOfList(listId: Long, page: Int): List<Movie> {
        return accountRepository.getMoviesOfList(listId, page)
    }

    suspend fun getSeriesOfList(listId: Long, page: Int): List<Series> {
        return accountRepository.getSeriesOfList(listId, page)
    }

    suspend fun addMovieToList(listId: Long, movieId: Long) {
        accountRepository.addMovieToList(listId, movieId)
    }

    suspend fun createList(listName: String) {
        accountRepository.createList(listName)
    }

    suspend fun removeMovieFromList(listId: Long, movieId: Long) {
        accountRepository.removeMovieFromList(listId, movieId)
    }
}