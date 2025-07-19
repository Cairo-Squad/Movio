package com.cairosquad.domain.repository

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review

interface MoviesRepository {
    suspend fun getMovie(movieId: Long): Movie

    suspend fun getMovieReviews(movieId: Long, page: Int): List<Review>

    suspend fun getSimilarMovies(movieId: Long, page: Int): List<Movie>

    suspend fun getMovieTopCast(movieId: Long, page: Int): List<Artist>

    suspend fun getPersonalizedMovies(page : Int): List<Movie>

    suspend fun getSuggestedMovies(): List<Movie>
}