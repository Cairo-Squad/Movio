package com.cairosquad.repository.account

import com.cairosquad.domain.repository.AccountRepository
import com.cairosquad.entity.Account
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.account.data_source.local.AccountLocalDataSource
import com.cairosquad.repository.account.data_source.local.toCacheDto
import com.cairosquad.repository.account.data_source.local.toEntity
import com.cairosquad.repository.account.data_source.remote.AccountRemoteDataSource
import com.cairosquad.repository.account.data_source.remote.toEntity
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountRemoteDataSource: AccountRemoteDataSource,
    private val accountLocalDataSource: AccountLocalDataSource
) : AccountRepository {
    override suspend fun getAccountDetails(): Account {
        return accountLocalDataSource
            .getAccount()
            .getOrNull(0)
            ?.toEntity()
            ?: accountRemoteDataSource
                .getAccountDetails()
                .toEntity()
                .also { account ->
                    accountLocalDataSource.setAccount(account.toCacheDto())
                }
    }

    override suspend fun getAccountId(): Long {
        return accountLocalDataSource.getAccountId() ?: 0L
    }

    override suspend fun getLists() {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteMovies(): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteSeries(): List<Series> {
        TODO("Not yet implemented")
    }

    override suspend fun addMovieToFavorite(movieId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun addSeriesToFavorite(seriesId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getRatedItems() {
        TODO("Not yet implemented")
    }
}