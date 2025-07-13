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

    override suspend fun getSeries(query: String): List<Series> = withContext(Dispatchers.IO) {
        localSearchCacheDataSource.getCachedSeries(query)
            .map { it.toSeries() }
            .takeIf { it.isNotEmpty() }
            ?: run {
                val remote = remoteSearchDataSource.getSeries(query).map { it.toSeries() }
                localSearchCacheDataSource.cacheSeries(
                    query,
                    remote.map { it.toSeriesCacheDto(query) }
                )
                remote
            }
    }

    override suspend fun getMovies(query: String): List<Movie> = withContext(Dispatchers.IO) {
        localSearchCacheDataSource.getCachedMovies(query)
            .map { it.toMovie() }
            .takeIf { it.isNotEmpty() }
            ?: run {
                val remote = remoteSearchDataSource.getMovies(query).map { it.toMovie() }
                localSearchCacheDataSource.cacheMovies(
                    query,
                    remote.map { it.toMovieCacheDto(query) }
                )
                remote
            }
    }

    override suspend fun getArtists(query: String): List<Artist> = withContext(Dispatchers.IO) {
        localSearchCacheDataSource.getCachedArtist(query)
            .map { it.toArtist() }
            .takeIf { it.isNotEmpty() }
            ?: run {
                val remote = remoteSearchDataSource.getArtists(query).map { it.toArtist() }
                localSearchCacheDataSource.cacheArtist(
                    query,
                    remote.map { it.toArtistCacheDto(query) }
                )
                remote
            }
    }
}
