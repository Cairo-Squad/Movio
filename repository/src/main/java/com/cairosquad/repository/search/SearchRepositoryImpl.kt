package com.cairosquad.repository.search

import com.cairosquad.domain.search.repository.SearchRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.common.exception.tryToCall
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
            tryToCall {
                val cachedSeries =
                    localSearchCacheDataSource.getCachedSeries(query).map { it.toEntity() }
                if (cachedSeries.isNotEmpty()) {
                    return@tryToCall cachedSeries
                } else {
                    val seriesResults =
                        remoteSearchDataSource.getSeries(query).map { it.toEntity() }
                    localSearchCacheDataSource.cacheSeries(
                        query,
                        seriesResults.map { it.toSeriesCacheDto(query) })
                    return@tryToCall seriesResults
                }
            }
    }

    override suspend fun getMovies(query: String): List<Movie> =
        withContext(Dispatchers.IO) {
            tryToCall {
                val cachedMovies = localSearchCacheDataSource.getCachedMovies(query).map { it.toEntity() }
                if (cachedMovies.isNotEmpty()) {
                    return@tryToCall cachedMovies
                } else {
                    val moviesResults = remoteSearchDataSource.getMovies(query).map { it.toEntity() }
                    localSearchCacheDataSource.cacheMovies(
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
                    localSearchCacheDataSource.getCachedArtist(query).map { it.toEntity() }
                if (cachedArtists.isNotEmpty()) {
                    return@tryToCall cachedArtists
                } else {
                    val artistResults =
                        remoteSearchDataSource.getArtists(query).map { it.toEntity() }
                    localSearchCacheDataSource.cacheArtist(
                        query,
                        artistResults.map { it.toArtistCacheDto(query) })
                    return@tryToCall artistResults
                }
            }
    }
}
