package com.cairosquad.repository.movie.data_source.local

import com.cairosquad.repository.movie.data_source.local.dto.GenreOfMovieCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.RequestWithMoviesCacheDto
import com.cairosquad.repository.utils.sharedDto.local.RequestWithReviewsCacheDto
import com.cairosquad.repository.utils.sharedDto.local.ReviewCacheDto

interface MoviesLocalDataSource {
    suspend fun insertRequestWithMovies(requestWithMovies: RequestWithMoviesCacheDto)

    suspend fun getMoviesByRequest(request: String): List<MovieCacheDto>

    suspend fun insertMovieGenres(genres: List<GenreOfMovieCacheDto>)

    suspend fun getMovieGenres(): List<GenreOfMovieCacheDto>

    suspend fun getMovieReviewsByRequest(request: String): List<ReviewCacheDto>

    suspend fun insertRequestWithReviews(requestWithReviewsCacheDto: RequestWithReviewsCacheDto)

    suspend fun deleteExpiredCache(timestamp: Long)
}