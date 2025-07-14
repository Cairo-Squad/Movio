package com.cairosquad.repository.search

import com.cairosquad.domain.search.repository.SearchRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.common.exception.tryToCall
import com.cairosquad.repository.search.data_source.local.LocalSearchCacheDataSource
import com.cairosquad.repository.search.data_source.local.dto.toCacheDto
import com.cairosquad.repository.search.data_source.local.dto.toEntity
import com.cairosquad.repository.search.data_source.remote.RemoteSearchDataSource
import com.cairosquad.repository.search.data_source.remote.dto.toEntity

class SearchRepositoryImpl(
    private val remoteSearchDataSource: RemoteSearchDataSource,
    private val localSearchCacheDataSource: LocalSearchCacheDataSource
) : SearchRepository {
    override suspend fun getSeries(query: String): List<Series> {
        return tryToCall {
            localSearchCacheDataSource.clearExpiredCache()
            localSearchCacheDataSource.getCachedSeries(query)
                .takeIf { it.isNotEmpty() }?.toEntity()
                ?: remoteSearchDataSource.getSeries(query).toEntity()
                    .also { result -> localSearchCacheDataSource.cacheSeries(result.toCacheDto(query)) }
        }
    }

    override suspend fun getMovies(query: String): List<Movie> {
        return tryToCall {
            localSearchCacheDataSource.clearExpiredCache()
            localSearchCacheDataSource.getCachedMovies(query)
                .takeIf { it.isNotEmpty() }?.toEntity()
                ?: remoteSearchDataSource.getMovies(query).toEntity()
                    .also { result -> localSearchCacheDataSource.cacheMovies(result.toCacheDto(query)) }
        }
    }

    override suspend fun getArtists(query: String): List<Artist> {
        return tryToCall {
            localSearchCacheDataSource.clearExpiredCache()
            localSearchCacheDataSource.getCachedArtists(query)
                .takeIf { it.isNotEmpty() }?.toEntity()
                ?: remoteSearchDataSource.getArtists(query).toEntity()
                    .also { result -> localSearchCacheDataSource.cacheArtist(result.toCacheDto(query)) }
        }
    }
}