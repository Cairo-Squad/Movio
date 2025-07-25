package com.cairosquad.repository.search

import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.search.data_source.local.CacheDataSource
import com.cairosquad.repository.search.data_source.local.LocalRecentSearchDataSource
import com.cairosquad.repository.search.data_source.local.dto.toCacheDto
import com.cairosquad.repository.search.data_source.local.dto.toEntity
import com.cairosquad.repository.search.data_source.local.dto.toEntityList
import com.cairosquad.repository.search.data_source.remote.RemoteSearchDataSource
import com.cairosquad.repository.search.data_source.remote.dto.toEntityList
import com.cairosquad.repository.utils.mappers.tryToCall
import java.util.Date

class SearchRepositoryImpl(
    private val remoteSearchDataSource: RemoteSearchDataSource,
    private val cacheDataSource: CacheDataSource,
    private val dataSource: LocalRecentSearchDataSource
) : SearchRepository {
    override suspend fun getSeries(query: String, page: Int): List<Series> {
        return tryToCall {
            cacheDataSource.clearExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
            cacheDataSource.getCachedSeries(query, page)
                .takeIf { it.isNotEmpty() }?.toEntity()
                ?: remoteSearchDataSource.getSeries(query, page).toEntityList()
                    .also { result -> cacheDataSource.cacheSeries(result.toCacheDto(query, page)) }
        }
    }

    override suspend fun getMovies(query: String, page: Int): List<Movie> {
        return tryToCall {
            cacheDataSource.clearExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
            cacheDataSource.getCachedMovies(query, page)
                .takeIf { it.isNotEmpty() }?.toEntity()
                ?: remoteSearchDataSource.getMovies(query, page).toEntityList()
                    .also { result -> cacheDataSource.cacheMovies(result.toCacheDto(query, page)) }
        }
    }

    override suspend fun getArtists(query: String, page: Int): List<Artist> {
        return tryToCall {
            cacheDataSource.clearExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
            cacheDataSource.getCachedArtists(query, page)
                .takeIf { it.isNotEmpty() }?.toEntityList()
                ?: remoteSearchDataSource.getArtists(query, page).toEntityList()
                    .also { result -> cacheDataSource.cacheArtist(result.toCacheDto(query, page)) }
        }
    }

    override suspend fun getAllHistory(): List<String> {
        return tryToCall { dataSource.getAll() }
    }

    override suspend fun getAllHistoryByQuery(query: String): List<String> {
        return tryToCall {
            query.takeIf { it.isNotBlank() }
                ?.let {
                    val series = remoteSearchDataSource.getSeries(query, 1)
                    val movies = remoteSearchDataSource.getMovies(query, 1)
                    val artist = remoteSearchDataSource.getArtists(query, 1)
                    val local = dataSource.getByQuery(it)
                    val merged = buildList {
                        addAll(local)
                        addAll(movies.map { it.title ?: "" })
                        addAll(artist.map { it.name ?: "" })
                        addAll(series.map { it.name ?: "" })
                    }
                    merged.distinct().shuffled().take(20)
                }
                ?: dataSource.getAll()
        }
    }

    override suspend fun clearAll() {
        tryToCall { dataSource.clearAll() }
    }

    override suspend fun removeQuery(query: String) {
        tryToCall { dataSource.removeQuery(query) }
    }

    override suspend fun addQuery(query: String) {
        tryToCall { dataSource.addQuery(query) }
    }

    private companion object {
        private const val CACHE_EXPIRATION_MILLIS = 3_600_000
    }
}