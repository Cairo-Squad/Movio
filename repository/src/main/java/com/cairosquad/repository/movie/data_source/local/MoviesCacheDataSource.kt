package com.cairosquad.repository.movie.data_source.local

import com.cairosquad.repository.movie.data_source.local.dto.MovieCacheDtoNew
import com.cairosquad.repository.movie.data_source.local.dto.RequestWithMoviesCacheDto

interface MoviesCacheDataSource {
    suspend fun cacheRequestWithMovies(requestWithMovies: RequestWithMoviesCacheDto)

    suspend fun getMoviesByRequest(request: String): List<MovieCacheDtoNew>

    suspend fun deleteExpiredCache(timestamp: Long)
}