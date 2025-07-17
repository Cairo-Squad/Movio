package com.cairosquad.domain.usecase.movies

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Movie

class GetPersonalizedMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun getPersonalizedMovies(): List<Movie> =
        moviesRepository.getPersonalizedMovies()
}