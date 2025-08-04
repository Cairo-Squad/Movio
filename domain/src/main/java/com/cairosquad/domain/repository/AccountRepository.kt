package com.cairosquad.domain.repository

import com.cairosquad.entity.Account
import com.cairosquad.entity.MediaList
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

interface AccountRepository {
	suspend fun getAccountDetails(): Account

	suspend fun getMovieLists(page: Int): List<MediaList>

	suspend fun getSeriesLists(page: Int): List<MediaList>

	suspend fun getFavoriteMovies(): List<Movie>

	suspend fun getFavoriteSeries(): List<Series>

	suspend fun addMovieToFavorite(movieId: Long)

	suspend fun addSeriesToFavorite(seriesId: Long)

	suspend fun getRatedItems()
}