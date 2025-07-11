package com.cairosquad.repository.search

import com.cairosquad.domain.search.repository.SearchRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.search.data_source.local.SearchCacheDataSource
import com.cairosquad.repository.search.data_source.remote.RemoteSearchDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepositoryImpl(
    private val remoteSearchDataSource: RemoteSearchDataSource,
    private val searchCacheDataSource: SearchCacheDataSource
) : SearchRepository {
    override suspend fun searchSeries(query: String): List<Series> =
        withContext(Dispatchers.IO) {
            val cachedSeries = searchCacheDataSource.getCachedSeries(query).map { it.toSeries() }
            if (cachedSeries.isNotEmpty()) {
                return@withContext cachedSeries
            } else {
                val seriesResults = remoteSearchDataSource.searchSeries(query).map { it.toSeries() }
                searchCacheDataSource.cacheSeries(query, seriesResults.map { it.toSeriesCacheDto(query) })
                return@withContext seriesResults
            }
        }

    override suspend fun searchMovies(query: String): List<Movie> =
        withContext(Dispatchers.IO) {
            val cachedMovies = searchCacheDataSource.getCachedMovies(query).map { it.toMovie() }
            if (cachedMovies.isNotEmpty()) {
                return@withContext cachedMovies
            } else {
                val moviesResults = remoteSearchDataSource.searchMovies(query).map { it.toMovie() }
                searchCacheDataSource.cacheMovies(query, moviesResults.map { it.toMovieCacheDto(query) })
                return@withContext moviesResults
            }

        }

    override suspend fun searchArtists(query: String): List<Artist> =
        withContext(Dispatchers.IO) {
            val cachedArtists = searchCacheDataSource.getCachedArtist(query).map { it.toArtist() }
            if (cachedArtists.isNotEmpty()) {
                return@withContext cachedArtists
            } else {
                val artistResults = remoteSearchDataSource.searchArtists(query).map { it.toArtist() }
                searchCacheDataSource.cacheArtist(query, artistResults.map { it.toArtistCacheDto(query) })
                return@withContext artistResults
            }
        }
}