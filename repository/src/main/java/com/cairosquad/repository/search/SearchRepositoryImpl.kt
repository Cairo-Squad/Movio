package com.cairosquad.repository.search

import com.cairosquad.domain.search.repository.SearchRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.common.exception.tryToCall
import com.cairosquad.repository.search.data_source.local.SearchCacheDataSource
import com.cairosquad.repository.search.data_source.remote.RemoteSearchDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepositoryImpl(
    private val remoteSearchDataSource: RemoteSearchDataSource,
    private val searchCacheDataSource: SearchCacheDataSource
) : SearchRepository {
    override suspend fun getSeries(query: String): List<Series> =
        withContext(Dispatchers.IO) {
            tryToCall {
                val cachedSeries =
                    searchCacheDataSource.getCachedSeries(query).map { it.toSeries() }
                if (cachedSeries.isNotEmpty()) {
                    return@tryToCall cachedSeries
                } else {
                    val seriesResults =
                        remoteSearchDataSource.getSeries(query).map { it.toSeries() }
                    searchCacheDataSource.cacheSeries(
                        query,
                        seriesResults.map { it.toSeriesCacheDto(query) })
                    return@tryToCall seriesResults
                }
            }
        }

    override suspend fun getMovies(query: String): List<Movie> =
        withContext(Dispatchers.IO) {
            tryToCall {
                val cachedMovies = searchCacheDataSource.getCachedMovies(query).map { it.toMovie() }
                if (cachedMovies.isNotEmpty()) {
                    return@tryToCall cachedMovies
                } else {
                    val moviesResults = remoteSearchDataSource.getMovies(query).map { it.toMovie() }
                    searchCacheDataSource.cacheMovies(
                        query,
                        moviesResults.map { it.toMovieCacheDto(query) })
                    return@tryToCall moviesResults
                }
            }
        }

    override suspend fun getArtists(query: String): List<Artist> =
        withContext(Dispatchers.IO) {
            tryToCall {
                val cachedArtists =
                    searchCacheDataSource.getCachedArtist(query).map { it.toArtist() }
                if (cachedArtists.isNotEmpty()) {
                    return@tryToCall cachedArtists
                } else {
                    val artistResults =
                        remoteSearchDataSource.getArtists(query).map { it.toArtist() }
                    searchCacheDataSource.cacheArtist(
                        query,
                        artistResults.map { it.toArtistCacheDto(query) })
                    return@tryToCall artistResults
                }
            }
        }
}