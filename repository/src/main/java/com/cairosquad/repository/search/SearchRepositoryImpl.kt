package com.cairosquad.repository.search

import com.cairosquad.domain.search.repository.SearchRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.common.exception.tryToCall
import com.cairosquad.repository.search.data_source.local.SearchCacheDataSource
import com.cairosquad.repository.search.data_source.local.dto.toCacheDto
import com.cairosquad.repository.search.data_source.local.dto.toEntity
import com.cairosquad.repository.search.data_source.remote.RemoteSearchDataSource
import com.cairosquad.repository.search.data_source.remote.dto.toEntity

class SearchRepositoryImpl(
    private val remoteSearchDataSource: RemoteSearchDataSource,
    private val searchCacheDataSource: SearchCacheDataSource
) : SearchRepository {
    override suspend fun getSeries(query: String): List<Series> {
        return tryToCall {
            searchCacheDataSource.clearExpiredCache()
            searchCacheDataSource.getCachedSeries(query)
                .takeIf { it.isNotEmpty() }?.map { it.toEntity() }
                ?: remoteSearchDataSource.getSeries(query).map { it.toEntity() }
                    .also { result -> searchCacheDataSource.cacheSeries(result.map { it.toCacheDto(query) }) }
        }
    }


    override suspend fun getMovies(query: String): List<Movie> {
        return tryToCall {
            searchCacheDataSource.clearExpiredCache()
            searchCacheDataSource.getCachedMovies(query)
                .takeIf { it.isNotEmpty() }?.map { it.toEntity() }
                ?: remoteSearchDataSource.getMovies(query).map { it.toEntity() }
                    .also { result -> searchCacheDataSource.cacheMovies(result.map { it.toCacheDto(query) }) }
        }
    }


    override suspend fun getArtists(query: String): List<Artist> {
        return tryToCall {
            searchCacheDataSource.clearExpiredCache()
            searchCacheDataSource.getCachedArtists(query)
                .takeIf { it.isNotEmpty() }?.map { it.toEntity() }
                ?: remoteSearchDataSource.getArtists(query).map { it.toEntity() }
                    .also { result -> searchCacheDataSource.cacheArtist(result.map { it.toCacheDto(query) }) }
        }
    }
}