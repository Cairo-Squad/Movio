package com.cairosquad.domain.usecase.movies

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Movie

class GetSuggestedMoviesUseCase(private val moviesRepository: MoviesRepository) {
    suspend fun getSuggestedMovies(): List<Movie> =
        moviesRepository.getSuggestedMovies()
}