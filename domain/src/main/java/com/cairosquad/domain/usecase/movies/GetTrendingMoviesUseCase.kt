package com.cairosquad.domain.usecase.movies

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Movie

class GetTrendingMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun getTrendingMovies(page:Int) : List<Movie>{
        return moviesRepository.getTrendingMovies(page)
    }
}