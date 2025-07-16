package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.MoviesRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review

class GetMoviesDetailsUseCase(
    private val moviesRepository: MoviesRepository
) {

    suspend fun getMovie(movieId: Long): Movie {
        return moviesRepository.getMovie(movieId)
    }

    suspend fun getMovieReviews(movieId: Long): List<Review> {
        return moviesRepository.getMovieReviews(movieId)
    }

    suspend fun getSimilarMovies(movieId: Long): List<Movie> {
        return moviesRepository.getSimilarMovies(movieId)
    }

    suspend fun getMovieTopCast(movieId: Long): List<Artist> {
        return moviesRepository.getMovieTopCast(movieId)
    }
}