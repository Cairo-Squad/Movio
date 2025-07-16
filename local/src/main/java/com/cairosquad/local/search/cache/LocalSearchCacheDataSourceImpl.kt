package com.cairosquad.local.search.cache

import com.cairosquad.local.search.cache.dao.CacheDao
import com.cairosquad.repository.search.data_source.local.dto.ArtistCacheDto
import com.cairosquad.repository.search.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.search.data_source.local.dto.SeriesCacheDto
import com.cairosquad.repository.search.data_source.local.LocalSearchCacheDataSource
import java.time.Instant

class LocalSearchCacheDataSourceImpl(
    private val cacheDao: CacheDao
): LocalSearchCacheDataSource {

    override suspend fun getCachedMovies(query: String,page:Int): List<MovieCacheDto> {
        return cacheDao.getCachedMovies(query,page)
    }

    override suspend fun cacheMovies(results: List<MovieCacheDto>) = cacheDao.cacheMovies(results)


    override suspend fun getCachedSeries(query: String): List<SeriesCacheDto> {
        return cacheDao.getCachedSeries(query)
    }

    override suspend fun cacheSeries(results: List<SeriesCacheDto>) = cacheDao.cacheSeries(results)


    override suspend fun getCachedArtists(query: String): List<ArtistCacheDto> {
        return cacheDao.getCachedArtist(query)
    }

    override suspend fun cacheArtist(results: List<ArtistCacheDto>) = cacheDao.cacheArtist(results)


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