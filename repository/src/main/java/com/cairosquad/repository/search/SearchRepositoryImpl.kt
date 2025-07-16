package com.cairosquad.repository.search

import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.common.exception.tryToCall
import com.cairosquad.repository.search.data_source.local.CacheDataSource
import com.cairosquad.repository.search.data_source.local.LocalRecentSearchDataSource
import com.cairosquad.repository.search.data_source.local.dto.toCacheDto
import com.cairosquad.repository.search.data_source.local.dto.toEntity
import com.cairosquad.repository.search.data_source.remote.RemoteSearchDataSource
import com.cairosquad.repository.search.data_source.remote.dto.toEntity
import java.util.Date

class SearchRepositoryImpl(
    private val remoteSearchDataSource: RemoteSearchDataSource,
    private val cacheDataSource: CacheDataSource,
    private val dataSource: LocalRecentSearchDataSource
) : SearchRepository {
    override suspend fun getSeries(query: String): List<Series> {
        return tryToCall {
            cacheDataSource.clearExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
            cacheDataSource.getCachedSeries(query)
                .takeIf { it.isNotEmpty() }?.toEntity()
                ?: remoteSearchDataSource.getSeries(query).toEntity()
                    .also { result -> cacheDataSource.cacheSeries(result.toCacheDto()) }
        }
    }

    override suspend fun getMovies(query: String): List<Movie> {
        return tryToCall {
            cacheDataSource.clearExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
            cacheDataSource.getCachedMovies(query)
                .takeIf { it.isNotEmpty() }?.toEntity()
                ?: remoteSearchDataSource.getMovies(query).toEntity()
                    .also { result -> cacheDataSource.cacheMovies(result.toCacheDto()) }
        }
    }

    override suspend fun getArtists(query: String): List<Artist> {
        return tryToCall {
            cacheDataSource.clearExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
            cacheDataSource.getCachedArtists(query)
                .takeIf { it.isNotEmpty() }?.toEntity()
                ?: remoteSearchDataSource.getArtists(query).toEntity()
                    .also { result -> cacheDataSource.cacheArtist(result.toCacheDto()) }
        }
    }

    override suspend fun getAllHistory(): List<String> {
        return tryToCall { dataSource.getAll() }
    }

    override suspend fun getAllHistoryByQuery(query: String): List<String> {
        return tryToCall {
            query.takeIf { it.isNotBlank() }
                ?.let { dataSource.getByQuery(it) }
                ?: dataSource.getAll()
        }
    }

    override suspend fun clearAll() {
        tryToCall { dataSource.clearAll() }
    }

    override suspend fun removeQuery(query: String) {
        tryToCall {dataSource.removeQuery(query)}
    }

    override suspend fun addQuery(query: String) {
        tryToCall { dataSource.addQuery(query) }
    }

    private companion object {
        private const val CACHE_EXPIRATION_MILLIS = 3_600_000
    }
}