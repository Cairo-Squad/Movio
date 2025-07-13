package com.cairosquad.repository.search

import com.cairosquad.domain.search.repository.SearchRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.search.data_source.local.LocalSearchCacheDataSource
import com.cairosquad.repository.search.data_source.remote.RemoteSearchDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepositoryImpl(
    private val remoteSearchDataSource: RemoteSearchDataSource,
    private val localSearchCacheDataSource: LocalSearchCacheDataSource
) : SearchRepository {
    override suspend fun getSeries(query: String): List<Series> =
        withContext(Dispatchers.IO) {
            val cachedSeries = localSearchCacheDataSource.getCachedSeries(query).map { it.toSeries() }
            if (cachedSeries.isNotEmpty()) {
                return@withContext cachedSeries
            } else {
                val seriesResults = remoteSearchDataSource.getSeries(query).map { it.toSeries() }
                localSearchCacheDataSource.cacheSeries(query, seriesResults.map { it.toSeriesCacheDto(query) })
                return@withContext seriesResults
            }
        }

    override suspend fun getMovies(query: String): List<Movie> =
        withContext(Dispatchers.IO) {
            val cachedMovies = localSearchCacheDataSource.getCachedMovies(query).map { it.toMovie() }
            if (cachedMovies.isNotEmpty()) {
                return@withContext cachedMovies
            } else {
                val moviesResults = remoteSearchDataSource.getMovies(query).map { it.toMovie() }
                localSearchCacheDataSource.cacheMovies(query, moviesResults.map { it.toMovieCacheDto(query) })
                return@withContext moviesResults
            }

        }

    override suspend fun getArtists(query: String): List<Artist> =
        withContext(Dispatchers.IO) {
            val cachedArtists = localSearchCacheDataSource.getCachedArtist(query).map { it.toArtist() }
            if (cachedArtists.isNotEmpty()) {
                return@withContext cachedArtists
            } else {
                val artistResults = remoteSearchDataSource.getArtists(query).map { it.toArtist() }
                localSearchCacheDataSource.cacheArtist(query, artistResults.map { it.toArtistCacheDto(query) })
                return@withContext artistResults
            }
        }
}