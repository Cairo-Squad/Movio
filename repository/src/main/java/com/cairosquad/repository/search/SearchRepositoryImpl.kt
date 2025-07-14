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
    override suspend fun getSeries(query: String): List<Series> = withContext(Dispatchers.IO) {
        tryToCall {
            localSearchCacheDataSource.getCachedSeries(query)
                .map { it.toEntity() }
                .takeIf { it.isNotEmpty() }
                ?: remoteSearchDataSource.getSeries(query)
                    .map { it.toEntity() }
                    .also { freshSeries ->
                        localSearchCacheDataSource.cacheSeries(
                            query,
                            freshSeries.map { it.toSeriesCacheDto(query) }
                        )
                    }
        }
    }

    override suspend fun getMovies(query: String): List<Movie> = withContext(Dispatchers.IO) {
        tryToCall {
            localSearchCacheDataSource.getCachedMovies(query)
                .map { it.toEntity() }
                .takeIf { it.isNotEmpty() }
                ?: remoteSearchDataSource.getMovies(query)
                    .map { it.toEntity() }
                    .also { freshMovies ->
                        localSearchCacheDataSource.cacheMovies(
                            query,
                            freshMovies.map { it.toMovieCacheDto(query) }
                        )
                    }
        }
    }

    override suspend fun getArtists(query: String): List<Artist> = withContext(Dispatchers.IO) {
        tryToCall {
            localSearchCacheDataSource.getCachedArtist(query)
                .map { it.toEntity() }
                .takeIf { it.isNotEmpty() }
                ?: remoteSearchDataSource.getArtists(query)
                    .map { it.toEntity() }
                    .also { freshArtists ->
                        localSearchCacheDataSource.cacheArtist(
                            query,
                            freshArtists.map { it.toArtistCacheDto(query) }
                        )
                    }
        }
    }
}
