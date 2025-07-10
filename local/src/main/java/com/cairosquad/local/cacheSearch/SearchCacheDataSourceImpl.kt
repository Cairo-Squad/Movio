package com.cairosquad.local.cacheSearch

import com.cairosquad.repository.dataSource.local.Dto.ArtistCacheDto
import com.cairosquad.repository.dataSource.local.Dto.MovieCacheDto
import com.cairosquad.repository.dataSource.local.Dto.SeriesCacheDto
import com.cairosquad.repository.dataSource.local.SearchCacheDataSource
import java.time.Instant

class SearchCacheDataSourceImpl(
    private val cacheDao: CacheDao
): SearchCacheDataSource {

    override suspend fun getCachedMovies(query: String): List<MovieCacheDto> {
        clearExpiredCache()
        return cacheDao.getCachedMovies(query).toDto()
    }

    override suspend fun cacheMovies(
        query: String,
        results: List<MovieCacheDto>
    ) {
        cacheDao.cacheMovies(
            results.toEntity(query, timestamp = Instant.now().toEpochMilli())
        )
    }

    override suspend fun getCachedSeries(query: String): List<SeriesCacheDto> {
        clearExpiredCache()
        return cacheDao.getCachedSeries(query).toDto()
    }

    override suspend fun cacheSeries(
        query: String,
        results: List<SeriesCacheDto>
    ) {
        cacheDao.cacheSeries(
            results.toEntity(query, timestamp = Instant.now().toEpochMilli())
        )
    }

    override suspend fun getCachedArtist(query: String): List<ArtistCacheDto> {
        clearExpiredCache()
        return cacheDao.getCachedArtist(query).toDto()
    }

    override suspend fun cacheArtist(
        query: String,
        results: List<ArtistCacheDto>
    ) {
        cacheDao.cacheArtist(
            results.toEntity(query, timestamp = Instant.now().toEpochMilli())
        )
    }

    private suspend fun clearExpiredCache() {
        val expirationTime = Instant.now().toEpochMilli() - CACHE_EXPIRATION_MILLIS
        cacheDao.deleteExpiredMoviesCache(expirationTime)
        cacheDao.deleteExpiredSeriesCache(expirationTime)
        cacheDao.deleteExpiredArtistCache(expirationTime)
    }

    private companion object {
        private const val CACHE_EXPIRATION_MILLIS = 3_600_000
    }
}