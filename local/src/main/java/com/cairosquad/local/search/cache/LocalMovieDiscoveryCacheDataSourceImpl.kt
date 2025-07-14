package com.cairosquad.local.search.cache

import com.cairosquad.local.search.cache.dao.CacheDao
import com.cairosquad.local.search.cache.dao.toDto
import com.cairosquad.local.search.cache.dao.toEntity
import com.cairosquad.repository.search.data_source.local.Dto.MovieCacheDto
import com.cairosquad.repository.search.data_source.local.LocalMovieDiscoveryCacheDataSource

class LocalMovieDiscoveryCacheDataSourceImpl(
    private val cacheDao: CacheDao
) : LocalMovieDiscoveryCacheDataSource {

    override suspend fun getCachedMovies(query: String): List<MovieCacheDto> {
        clearExpiredCache()
        return cacheDao.getCachedMovies(query).map { it.toDto() }
    }

    override suspend fun cacheMovies(
        query: String,
        results: List<MovieCacheDto>
    ) {
        cacheDao.cacheMovies(results.map { it.toEntity() })
    }

    private suspend fun clearExpiredCache() {
        val expirationTime = System.currentTimeMillis() - CACHE_EXPIRATION_MILLIS
        cacheDao.deleteExpiredMoviesCache(expirationTime)
    }

    companion object {
        private const val CACHE_EXPIRATION_MILLIS = 60 * 60 * 1000L
    }
}
