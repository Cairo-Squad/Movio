package com.cairosquad.domain.search.repository

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review

interface MoviesDetailsRepository {
    suspend fun getMovieById(movieId: Long): Movie

    suspend fun getReviewsByMovieId(movieId: Long): List<Review>

    suspend fun getSimilarMovies(movieId: Long): List<Movie>

    suspend fun getTopCastByMovieId(movieId: Long): List<Artist>
}