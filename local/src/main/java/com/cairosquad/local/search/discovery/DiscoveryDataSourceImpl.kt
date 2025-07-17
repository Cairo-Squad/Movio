package com.cairosquad.local.search.discovery

import com.cairosquad.local.search.discovery.dao.DiscoveryDao
import com.cairosquad.repository.common.mappers.toPersonalizedMoviesIdsDto
import com.cairosquad.repository.common.mappers.toSuggestedMoviesIds
import com.cairosquad.repository.search.data_source.local.CacheDataSource
import com.cairosquad.repository.search.data_source.local.DiscoveryDataSource
import com.cairosquad.repository.search.data_source.local.dto.MovieCacheDto

class DiscoveryDataSourceImpl(
    private val cacheDataSource: CacheDataSource,
    private val discoveryDao: DiscoveryDao
): DiscoveryDataSource {
    override suspend fun getPersonalizedMovies(): List<MovieCacheDto> {
        return discoveryDao.getPersonalizedMoviesIds()
    }

    override suspend fun cachePersonalizedMovies(movies: List<MovieCacheDto>) {
        discoveryDao.cachePersonalizedMoviesIds(movies.toPersonalizedMoviesIdsDto())
        cacheDataSource.cacheMovies(movies)
    }

    override suspend fun getSuggestedMovies(): List<MovieCacheDto> {
        return discoveryDao.getSuggestedMovies()
    }

    override suspend fun cacheSuggestedMovies(movies: List<MovieCacheDto>) {
        discoveryDao.cacheSuggestedMovies(movies.toSuggestedMoviesIds())
        cacheDataSource.cacheMovies(movies)
    }

    override suspend fun clearExpiredCache(expirationTime: Long) {
        discoveryDao.deleteExpiredPersonalizedMoviesId(expirationTime)
        discoveryDao.deleteExpiredPersonalizedMoviesId(expirationTime)
        cacheDataSource.clearExpiredCache(expirationTime)
    }
}