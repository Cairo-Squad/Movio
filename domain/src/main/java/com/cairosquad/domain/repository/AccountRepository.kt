package com.cairosquad.domain.repository

import com.cairosquad.entity.Account
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

interface AccountRepository {
	suspend fun getAccountDetails(): Account

	suspend fun getLists()

	suspend fun getFavoriteMovies(): List<Movie>

	suspend fun getFavoriteSeries(): List<Series>

	suspend fun addMovieToFavorite(movieId: Long)

	suspend fun addSeriesToFavorite(seriesId: Long)

	suspend fun getRatedItems()
}