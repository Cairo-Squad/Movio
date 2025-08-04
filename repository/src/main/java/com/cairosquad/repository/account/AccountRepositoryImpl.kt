package com.cairosquad.repository.account

import com.cairosquad.domain.repository.AccountRepository
import com.cairosquad.entity.Account
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.account.data_source.remote.AccountRemoteDataSource
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
	private val accountRemoteDataSource: AccountRemoteDataSource
): AccountRepository {
	override suspend fun getAccountDetails(): Account {
		TODO("Not yet implemented")
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