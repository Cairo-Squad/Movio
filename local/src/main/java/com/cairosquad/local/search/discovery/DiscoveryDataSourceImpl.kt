package com.cairosquad.local.search.discovery

import com.cairosquad.local.search.discovery.dao.DiscoveryDao
import com.cairosquad.repository.search.data_source.local.CacheDataSource
import com.cairosquad.repository.search.data_source.local.DiscoveryDataSource
import com.cairosquad.repository.search.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.utils.mappers.toPersonalizedMoviesIdsDtoList
import com.cairosquad.repository.utils.mappers.toSuggestedMoviesIdsList

class DiscoveryDataSourceImpl(
    private val cacheDataSource: CacheDataSource,
    private val discoveryDao: DiscoveryDao
): DiscoveryDataSource {
    override suspend fun getPersonalizedMovies(page : Int): List<MovieCacheDto> {
        return discoveryDao.getPersonalizedMovies(page)
    }

    override suspend fun cachePersonalizedMovies(movies: List<MovieCacheDto>) {
        discoveryDao.cachePersonalizedMoviesIds(movies.toPersonalizedMoviesIdsDtoList())
        cacheDataSource.cacheMovies(movies)
    }

    override suspend fun getSuggestedMovies(): List<MovieCacheDto> {
        return discoveryDao.getSuggestedMovies()
    }

    override suspend fun cacheSuggestedMovies(movies: List<MovieCacheDto>) {
        discoveryDao.cacheSuggestedMovies(movies.toSuggestedMoviesIdsList())
        cacheDataSource.cacheMovies(movies)
    }

    override suspend fun clearExpiredCache(expirationTime: Long) {
        discoveryDao.deleteExpiredPersonalizedMoviesId(expirationTime)
        discoveryDao.deleteExpiredPersonalizedMoviesId(expirationTime)
        cacheDataSource.clearExpiredCache(expirationTime)
    }
}