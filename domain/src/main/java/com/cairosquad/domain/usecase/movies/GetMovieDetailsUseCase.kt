package com.cairosquad.domain.usecase.movies

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review

class GetMovieDetailsUseCase(
    private val moviesRepository: MoviesRepository
) {

    suspend fun getMovie(movieId: Long): Movie {
        return moviesRepository.getMovie(movieId)
    }

    suspend fun getMovieReviews(movieId: Long, page: Int = 1): List<Review> {
        return moviesRepository.getMovieReviews(movieId, page)
    }

    suspend fun getSimilarMovies(movieId: Long, page: Int = 1): List<Movie> {
        return moviesRepository.getSimilarMovies(movieId, page)
    }

    suspend fun getMovieTopCast(movieId: Long, page: Int = 1): List<Artist> {
        return moviesRepository.getMovieTopCast(movieId, page)
    }
}