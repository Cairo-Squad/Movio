package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.MoviesDetailsRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review

class GetMoviesDetailsUseCase(
    private val moviesDetailsRepository: MoviesDetailsRepository
) {

    suspend fun getMovieById(movieId: Long): Movie {
        return moviesDetailsRepository.getMovieById(movieId)
    }

    suspend fun getReviewsByMovieId(movieId: Long): List<Review> {
        return moviesDetailsRepository.getReviewsByMovieId(movieId)
    }

    suspend fun getSimilarMovies(movieId: Long): List<Movie> {
        return moviesDetailsRepository.getSimilarMovies(movieId)
    }

    suspend fun getTopCastByMovieId(movieId: Long): List<Artist> {
        return moviesDetailsRepository.getTopCastByMovieId(movieId)
    }
}