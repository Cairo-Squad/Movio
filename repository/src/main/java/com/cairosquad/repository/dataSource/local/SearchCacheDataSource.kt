package com.cairosquad.repository.dataSource.local

import com.cairosquad.repository.dataSource.local.Dto.ArtistCacheDto
import com.cairosquad.repository.dataSource.local.Dto.MovieCacheDto
import com.cairosquad.repository.dataSource.local.Dto.SeriesCacheDto

interface SearchCacheDataSource {
    suspend fun getCachedMovies(query: String): List<MovieCacheDto>
    suspend fun cacheMovies(query: String, results: List<MovieCacheDto>)

    suspend fun getCachedSeries(query: String): List<SeriesCacheDto>
    suspend fun cacheSeries(query: String, results: List<SeriesCacheDto>)

    suspend fun getCachedArtist(query: String): List<ArtistCacheDto>
    suspend fun cacheArtist(query: String, results: List<ArtistCacheDto>)
}