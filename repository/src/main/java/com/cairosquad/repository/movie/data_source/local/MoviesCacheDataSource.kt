package com.cairosquad.repository.movie.data_source.local

import com.cairosquad.repository.movie.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.MovieGenreCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.RequestWithMoviesCacheDto
import com.cairosquad.repository.utils.sharedDto.local.RequestWithReviewsCacheDto
import com.cairosquad.repository.utils.sharedDto.local.ReviewCacheDto

interface MoviesCacheDataSource {
    suspend fun cacheRequestWithMovies(requestWithMovies: RequestWithMoviesCacheDto)

    suspend fun getMoviesByRequest(request: String): List<MovieCacheDto>

    suspend fun cacheMovieGenres(genres: List<MovieGenreCacheDto>)

    suspend fun getMovieGenres(): List<MovieGenreCacheDto>

    suspend fun getMovieReviewsByRequest(request: String): List<ReviewCacheDto>

    suspend fun cacheRequestWithReviews(requestWithReviewsCacheDto: RequestWithReviewsCacheDto)

    suspend fun deleteExpiredCache(timestamp: Long)
}