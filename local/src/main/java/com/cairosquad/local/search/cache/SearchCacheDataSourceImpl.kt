package com.cairosquad.local.search.cache

import com.cairosquad.local.search.cache.dao.CacheDao
import com.cairosquad.local.search.cache.dao.toDto
import com.cairosquad.local.search.cache.dao.toEntity
import com.cairosquad.repository.search.data_source.local.Dto.ArtistCacheDto
import com.cairosquad.repository.search.data_source.local.Dto.MovieCacheDto
import com.cairosquad.repository.search.data_source.local.Dto.SeriesCacheDto
import com.cairosquad.repository.search.data_source.local.SearchCacheDataSource
import java.time.Instant

class SearchCacheDataSourceImpl(
    private val cacheDao: CacheDao
): SearchCacheDataSource {

    override suspend fun getCachedMovies(query: String): List<MovieCacheDto> {
        clearExpiredCache()
        return cacheDao.getCachedMovies(query).map { movieCacheEntity ->
            movieCacheEntity.toDto()
        }
    }

    override suspend fun cacheMovies(
        query: String,
        results: List<MovieCacheDto>
    ) {
        cacheDao.cacheMovies(
            results.map { movieCacheDto ->
                movieCacheDto.toEntity()
            }
        )
    }

    override suspend fun getCachedSeries(query: String): List<SeriesCacheDto> {
        clearExpiredCache()
        return cacheDao.getCachedSeries(query).map { movieCacheEntity ->
            movieCacheEntity.toDto()
        }
    }

    override suspend fun cacheSeries(
        query: String,
        results: List<SeriesCacheDto>
    ) {
        cacheDao.cacheSeries(
            results.map { movieCacheDto ->
                movieCacheDto.toEntity()
            }
        )
    }

    override suspend fun getCachedArtist(query: String): List<ArtistCacheDto> {
        clearExpiredCache()
        return cacheDao.getCachedArtist(query).map { movieCacheEntity ->
            movieCacheEntity.toDto()
        }
    }

    override suspend fun cacheArtist(
        query: String,
        results: List<ArtistCacheDto>
    ) {
        cacheDao.cacheArtist(
            results.map { movieCacheDto ->
                movieCacheDto.toEntity()
            }
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