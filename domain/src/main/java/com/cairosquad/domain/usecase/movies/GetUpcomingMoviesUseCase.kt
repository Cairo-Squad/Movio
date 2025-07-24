package com.cairosquad.domain.usecase.movies

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Movie

class GetUpcomingMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun getUpcomingMovies(page: Int, categoryId: String? = null): List<Movie>{
        return moviesRepository.getUpcomingMovies(page,categoryId)
    }
}