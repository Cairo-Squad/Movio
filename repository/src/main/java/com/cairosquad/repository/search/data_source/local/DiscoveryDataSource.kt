package com.cairosquad.repository.search.data_source.local

import com.cairosquad.repository.search.data_source.local.dto.MovieCacheDto

interface DiscoveryDataSource {

    suspend fun getPersonalizedMovies(page : Int): List<MovieCacheDto>

    suspend fun cachePersonalizedMovies(movies: List<MovieCacheDto>)

    suspend fun getSuggestedMovies(): List<MovieCacheDto>

    suspend fun cacheSuggestedMovies(movies: List<MovieCacheDto>)

    suspend fun clearExpiredCache(expirationTime: Long)

}