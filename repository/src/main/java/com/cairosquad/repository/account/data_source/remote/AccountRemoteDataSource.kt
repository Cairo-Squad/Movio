package com.cairosquad.repository.account.data_source.remote

import com.cairosquad.repository.account.data_source.remote.dto.MediaListDto
import com.cairosquad.repository.account.data_source.remote.dto.acount.AccountDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto

interface AccountRemoteDataSource {

    suspend fun getAccountDetails(): AccountDto

    suspend fun getMovieLists(accountId: Long, page: Int): List<MediaListDto>

    suspend fun getSeriesLists(accountId: Long, page: Int): List<MediaListDto>

    suspend fun getMoviesOfList(listId: Long, page: Int): List<MovieRemoteDto>

    suspend fun getSeriesOfList(listId: Long, page: Int): List<SeriesRemoteDto>

    suspend fun addMovieToFavorite(accountId: Long, movieId: Long)

    suspend fun addSeriesToFavorite(accountId: Long, seriesId: Long)

    suspend fun removeMovieFromFavorite(accountId: Long, movieId: Long)

    suspend fun removeSeriesFromFavorite(accountId: Long, seriesId: Long)

    suspend fun getFavoriteMovies(accountId: Long, page: Int): List<MovieRemoteDto>

    suspend fun getFavoriteSeries(accountId: Long, page: Int): List<SeriesRemoteDto>

    suspend fun addMovieToHistory(accountId: Long, movieId: Long)

    suspend fun addSeriesToHistory(accountId: Long, seriesId: Long)

    suspend fun getHistoryMovies(accountId: Long, page: Int): List<MovieRemoteDto>

    suspend fun getHistorySeries(accountId: Long, page: Int): List<SeriesRemoteDto>

    suspend fun getRatedMovies(accountId: Long, page: Int): List<MovieRemoteDto>

    suspend fun getRatedSeries(accountId: Long, page: Int): List<SeriesRemoteDto>

    suspend fun addMovieToList(listId: Long, movieId: Long)

    suspend fun createList(listName: String)

    suspend fun removeMovieFromList(listId: Long, movieId: Long)

    suspend fun removeMovieFromHistory(accountId: Long, movieId: Long)

    suspend fun removeSeriesFromHistory(accountId: Long, seriesId: Long)
}