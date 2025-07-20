package com.cairosquad.domain.repository

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review

interface MoviesRepository {
    suspend fun getMovie(movieId: Long): Movie

    suspend fun getMovieReviews(movieId: Long, page: Int): List<Review>

    suspend fun getSimilarMovies(movieId: Long, page: Int): List<Movie>

    suspend fun getMovieTopCast(movieId: Long, page: Int): List<Artist>

    suspend fun getPersonalizedMovies(page: Int): List<Movie>

    suspend fun getSuggestedMovies(): List<Movie>

    suspend fun getTopRatingMovies(page: Int): List<Movie>

    suspend fun getUpcomingMovies(page: Int): List<Movie>

    suspend fun getNowPlayingMovies(page: Int): List<Movie>

    suspend fun getTrendingMovies(page: Int): List<Movie>

    suspend fun getMoreRecommendedMovies(page: Int): List<Movie>

    suspend fun getFreeToWatchMovies(page: Int): List<Movie>

    suspend fun getMoviesByCategory(categoryId: String, page: Int): List<Movie>

    suspend fun getRandomMoviesUseCase(page: Int): List<Movie>

}