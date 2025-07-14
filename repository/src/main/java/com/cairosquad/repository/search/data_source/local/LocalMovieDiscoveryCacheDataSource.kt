package com.cairosquad.repository.search.data_source.local

import com.cairosquad.repository.search.data_source.local.Dto.MovieCacheDto

interface LocalMovieDiscoveryCacheDataSource {
    suspend fun getCachedMovies(query: String): List<MovieCacheDto>
    suspend fun cacheMovies(query: String, results: List<MovieCacheDto>)
}
