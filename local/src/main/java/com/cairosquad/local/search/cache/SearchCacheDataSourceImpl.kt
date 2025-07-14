package com.cairosquad.local.search.cache

import com.cairosquad.local.search.cache.dao.CacheDao
import com.cairosquad.repository.search.data_source.local.dto.CachedArtistDto
import com.cairosquad.repository.search.data_source.local.dto.CachedMovieDto
import com.cairosquad.repository.search.data_source.local.dto.CachedSeriesDto
import com.cairosquad.repository.search.data_source.local.SearchCacheDataSource
import java.time.Instant

class SearchCacheDataSourceImpl(
    private val cacheDao: CacheDao
): SearchCacheDataSource {

    override suspend fun getCachedMovies(query: String): List<CachedMovieDto> {
        return cacheDao.getCachedMovies(query)
    }

    override suspend fun cacheMovies(results: List<CachedMovieDto>) = cacheDao.cacheMovies(results)


    override suspend fun getCachedSeries(query: String): List<CachedSeriesDto> {
        return cacheDao.getCachedSeries(query)
    }

    override suspend fun cacheSeries(results: List<CachedSeriesDto>) = cacheDao.cacheSeries(results)


    override suspend fun getCachedArtists(query: String): List<CachedArtistDto> {
        return cacheDao.getCachedArtist(query)
    }

    override suspend fun cacheArtist(results: List<CachedArtistDto>) = cacheDao.cacheArtist(results)


    override suspend fun clearExpiredCache() {
        val expirationTime = Instant.now().toEpochMilli() - CACHE_EXPIRATION_MILLIS
        cacheDao.deleteExpiredMoviesCache(expirationTime)
        cacheDao.deleteExpiredSeriesCache(expirationTime)
        cacheDao.deleteExpiredArtistCache(expirationTime)
    }

    private companion object {
        private const val CACHE_EXPIRATION_MILLIS = 3_600_000
    }
}