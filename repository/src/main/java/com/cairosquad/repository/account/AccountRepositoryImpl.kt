package com.cairosquad.repository.account

import com.cairosquad.domain.repository.AccountRepository
import com.cairosquad.entity.Account
import com.cairosquad.entity.MediaList
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.account.data_source.local.AccountLocalDataSource
import com.cairosquad.repository.account.data_source.local.toCacheDto
import com.cairosquad.repository.account.data_source.local.toEntity
import com.cairosquad.repository.account.data_source.remote.AccountRemoteDataSource
import com.cairosquad.repository.account.data_source.remote.toEntity
import com.cairosquad.repository.movie.data_source.remote.toEntity
import com.cairosquad.repository.series.data_source.remote.toEntity
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountRemoteDataSource: AccountRemoteDataSource,
    private val accountLocalDataSource: AccountLocalDataSource
) : AccountRepository {
    override suspend fun getAccountDetails(): Account {
        return try {
            accountRemoteDataSource
                .getAccountDetails()
                .toEntity()
                .also { account ->
                    accountLocalDataSource.setAccount(account.toCacheDto())
                }
        } catch (e: Exception) {
            accountLocalDataSource
                .getAccount()
                .getOrNull(0)
                ?.toEntity()
                ?: throw e
        }
    }

    override suspend fun getMovieLists(page: Int): List<MediaList> {
        accountLocalDataSource.getAccountId()?.also { accountId ->
            return accountRemoteDataSource.getMovieLists(accountId, page).map { it.toEntity() }
        }
        return emptyList()
    }

    override suspend fun getSeriesLists(page: Int): List<MediaList> {
        accountLocalDataSource.getAccountId()?.also { accountId ->
            return accountRemoteDataSource.getSeriesLists(accountId, page).map { it.toEntity() }
        }
        return emptyList()
    }

    override suspend fun getFavoriteMovies(page: Int): List<Movie> {
        accountLocalDataSource.getAccountId()?.also { accountId ->
            return accountRemoteDataSource.getFavoriteMovies(accountId, page).map { it.toEntity() }
        }
        return emptyList()
    }

    override suspend fun getFavoriteSeries(page: Int): List<Series> {
        accountLocalDataSource.getAccountId()?.also { accountId ->
            return accountRemoteDataSource.getFavoriteSeries(accountId, page).map { it.toEntity() }
        }
        return emptyList()
    }

    override suspend fun getMoviesOfList(
        listId: Long,
        page: Int
    ): List<Movie> {
        accountLocalDataSource.getAccount().also {  accountId ->
            return  accountRemoteDataSource.getMoviesOfList(listId, page).map { it.toEntity() }
        }
    }

    override suspend fun getSeriesOfList(
        listId: Long,
        page: Int
    ): List<Series> {
        accountLocalDataSource.getAccount().also {  accountId ->
            return accountRemoteDataSource.getSeriesOfList(listId, page).map { it.toEntity() }
        }
    }

    override suspend fun addMovieToFavorite(movieId: Long) {
        accountLocalDataSource.getAccountId()?.also { accountId ->
            accountRemoteDataSource.addMovieToFavorite(accountId, movieId)
        }
    }

    override suspend fun addSeriesToFavorite(seriesId: Long) {
        accountLocalDataSource.getAccountId()?.also { accountId ->
            accountRemoteDataSource.addSeriesToFavorite(accountId, seriesId)
        }
    }

    override suspend fun removeMovieFromFavorite(movieId: Long) {
        accountLocalDataSource.getAccountId()?.also { accountId ->
            accountRemoteDataSource.removeMovieFromFavorite(accountId, movieId)
        }
    }

    override suspend fun removeSeriesFromFavorite(seriesId: Long) {
        accountLocalDataSource.getAccountId()?.also { accountId ->
            accountRemoteDataSource.removeSeriesFromFavorite(accountId, seriesId)
        }
    }

    override suspend fun addMovieToHistory(movieId: Long) {
        accountLocalDataSource.getAccountId()?.also { accountId ->
            accountRemoteDataSource.addMovieToHistory(accountId, movieId)
        }
    }

    override suspend fun addSeriesToHistory(seriesId: Long) {
        accountLocalDataSource.getAccountId()?.also { accountId ->
            accountRemoteDataSource.addSeriesToHistory(accountId, seriesId)
        }    }

    override suspend fun getHistoryMovies(page: Int): List<Movie> {
        accountLocalDataSource.getAccountId()?.also { accountId ->
            return accountRemoteDataSource.getHistoryMovies(accountId, page).map { it.toEntity() }
        }
        return emptyList()
    }

    override suspend fun getHistorySeries(page: Int): List<Series> {
        accountLocalDataSource.getAccountId()?.also { accountId ->
            return accountRemoteDataSource.getHistorySeries(accountId, page).map { it.toEntity() }
        }
        return emptyList()
    }

    override suspend fun getRatedItems(page: Int): Pair<List<Movie>, List<Series>> {
        accountLocalDataSource.getAccountId()?.also { accountId ->
            return Pair(
                accountRemoteDataSource.getRatedMovies(accountId, page).map { it.toEntity() },
                accountRemoteDataSource.getRatedSeries(accountId, page).map { it.toEntity() }
            )
        }
        return Pair(emptyList(), emptyList())
    }

    override suspend fun addMovieToList(listId: Long, movieId: Long) {
        accountRemoteDataSource.addMovieToList(listId, movieId)
    }

    override suspend fun createList(listName: String) {
        accountRemoteDataSource.createList(listName)
    }

    override suspend fun removeMovieFromList(listId: Long, movieId: Long) {
        accountRemoteDataSource.removeMovieFromList(listId, movieId)
    }

    override suspend fun removeAccountDetails() {
        accountLocalDataSource.removeAccount()
    }
}