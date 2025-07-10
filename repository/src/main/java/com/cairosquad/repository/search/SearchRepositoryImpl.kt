package com.cairosquad.repository.search

import com.cairosquad.domain.search.repository.SearchRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.search.dataSource.remote.search.RemoteSearchDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepositoryImpl(
    private val remoteSearchDataSource: RemoteSearchDataSource
) : SearchRepository {
    override suspend fun searchSeries(query: String): List<Series> =
        withContext(Dispatchers.IO) {
            remoteSearchDataSource.searchSeries(query).map { it.toSeries() }
        }

    override suspend fun searchMovies(query: String): List<Movie> =
        withContext(Dispatchers.IO) {
            remoteSearchDataSource.searchMovies(query).map { it.toMovie() }
        }

    override suspend fun searchArtists(query: String): List<Artist> =
        withContext(Dispatchers.IO) {
            remoteSearchDataSource.searchArtists(query).map { it.toArtist() }
        }
}