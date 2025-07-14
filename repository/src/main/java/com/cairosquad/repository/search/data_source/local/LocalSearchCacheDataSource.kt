package com.cairosquad.repository.search.data_source.local

import com.cairosquad.repository.search.data_source.local.dto.CachedArtistDto
import com.cairosquad.repository.search.data_source.local.dto.CachedMovieDto
import com.cairosquad.repository.search.data_source.local.dto.CachedSeriesDto

interface LocalSearchCacheDataSource {
    suspend fun getCachedMovies(query: String): List<CachedMovieDto>
    suspend fun cacheMovies(results: List<CachedMovieDto>)

    suspend fun getCachedSeries(query: String): List<CachedSeriesDto>
    suspend fun cacheSeries(results: List<CachedSeriesDto>)

    suspend fun getCachedArtists(query: String): List<CachedArtistDto>
    suspend fun cacheArtist(results: List<CachedArtistDto>)

    suspend fun clearExpiredCache()
}