package com.cairosquad.repository.search.data_source.local

import com.cairosquad.repository.search.data_source.local.dto.ArtistCacheDto
import com.cairosquad.repository.search.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.search.data_source.local.dto.SeriesCacheDto

interface LocalSearchCacheDataSource {
    suspend fun getCachedMovies(query: String): List<MovieCacheDto>
    suspend fun cacheMovies(results: List<MovieCacheDto>)

    suspend fun getCachedSeries(query: String): List<SeriesCacheDto>
    suspend fun cacheSeries(results: List<SeriesCacheDto>)

    suspend fun getCachedArtists(query: String): List<ArtistCacheDto>
    suspend fun cacheArtist(results: List<ArtistCacheDto>)

    suspend fun clearExpiredCache()
}