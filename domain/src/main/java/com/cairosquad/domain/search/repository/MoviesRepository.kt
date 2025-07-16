package com.cairosquad.domain.search.repository

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review

interface MoviesRepository {
    suspend fun getMovie(movieId: Long): Movie

    suspend fun getMovieReviews(movieId: Long): List<Review>

    suspend fun getSimilarMovies(movieId: Long): List<Movie>

    suspend fun getMovieTopCast(movieId: Long): List<Artist>
}