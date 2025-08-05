package com.cairosquad.domain.repository

import com.cairosquad.entity.Account
import com.cairosquad.entity.MediaList
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

interface AccountRepository {
    suspend fun getAccountDetails(): Account

    suspend fun getMovieLists(page: Int): List<MediaList>

    suspend fun getSeriesLists(page: Int): List<MediaList>

    suspend fun getFavoriteMovies(page: Int): List<Movie>

    suspend fun getFavoriteSeries(page: Int): List<Series>

    suspend fun addMovieToFavorite(movieId: Long)

    suspend fun addSeriesToFavorite(seriesId: Long)


    suspend fun addMovieToHistory(movieId: Long)

    suspend fun addSeriesToHistory(seriesId: Long)

    suspend fun getHistoryMovies(page: Int): List<Movie>

    suspend fun getHistorySeries(page: Int): List<Series>

    suspend fun getRatedItems(page: Int): Pair<List<Movie>, List<Series>>
}