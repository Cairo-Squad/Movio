package com.cairosquad.domain.usecase.movies

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Movie

class GetPopularMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun getPopularMovies(page: Int, categoryId: String? = null) : List<Movie>{
        return moviesRepository.getPopularMovies(page,categoryId)
    }
}