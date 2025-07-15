package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.MoviesDetailsRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review

class GetMoviesDetailsUseCase(
    private val moviesDetailsRepository: MoviesDetailsRepository
) {

    suspend fun getMovie(movieId: Long): Movie {
        return moviesDetailsRepository.getMovie(movieId)
    }

    suspend fun getMovieReviews(movieId: Long): List<Review> {
        return moviesDetailsRepository.getMovieReviews(movieId)
    }

    suspend fun getSimilarMovies(movieId: Long): List<Movie> {
        return moviesDetailsRepository.getSimilarMovies(movieId)
    }

    suspend fun getMovieTopCast(movieId: Long): List<Artist> {
        return moviesDetailsRepository.getMovieTopCast(movieId)
    }
}