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
    override suspend fun getSeries(query: String,page:Int): List<Series> {
        return tryToCall {
            localSearchCacheDataSource.clearExpiredCache()
            localSearchCacheDataSource.getCachedSeries(query,page)
                .takeIf { it.isNotEmpty() }?.toEntity()
                ?: remoteSearchDataSource.getSeries(query,page).toEntity()
                    .also { result -> localSearchCacheDataSource.cacheSeries(result.toCacheDto(query,page)) }
        }
    }

    override suspend fun getMovies(query: String,page:Int): List<Movie> {
        return tryToCall {
            localSearchCacheDataSource.clearExpiredCache()
            localSearchCacheDataSource.getCachedMovies(query,page)
                .takeIf { it.isNotEmpty() }?.toEntity()
                ?: remoteSearchDataSource.getMovies(query,page).toEntity()
                    .also { result -> localSearchCacheDataSource.cacheMovies(result.toCacheDto(query,page)) }
        }
    }

    override suspend fun getArtists(query: String,page:Int): List<Artist> {
        return tryToCall {
            localSearchCacheDataSource.clearExpiredCache()
            localSearchCacheDataSource.getCachedArtists(query,page)
                .takeIf { it.isNotEmpty() }?.toEntity()
                ?: remoteSearchDataSource.getArtists(query,page).toEntity()
                    .also { result -> localSearchCacheDataSource.cacheArtist(result.toCacheDto(query,page)) }
        }
    }
}