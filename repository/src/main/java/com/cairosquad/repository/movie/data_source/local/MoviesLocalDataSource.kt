package com.cairosquad.repository.movie.data_source.local

import com.cairosquad.repository.movie.data_source.local.dto.CacheCodeWithMoviesCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.GenreOfMovieCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeWithReviewsCacheDto
import com.cairosquad.repository.utils.sharedDto.local.ReviewCacheDto

interface MoviesLocalDataSource {
    suspend fun insertCacheCodeWithMovies(cacheCodeWithMovies: CacheCodeWithMoviesCacheDto)

    suspend fun getMoviesByCacheCode(cacheCode: String): List<MovieCacheDto>

    suspend fun insertMovieGenres(genres: List<GenreOfMovieCacheDto>)

    suspend fun getMovieGenres(): List<GenreOfMovieCacheDto>

    suspend fun getMovieGenresByLanguage(language: String): List<GenreOfMovieCacheDto>

    suspend fun getMovieReviewsByCacheCode(cacheCode: String): List<ReviewCacheDto>

    suspend fun insertCacheCodeWithReviews(cacheCodeWithReviewsCacheDto: CacheCodeWithReviewsCacheDto)

    suspend fun deleteExpiredCache(timestamp: Long)
}