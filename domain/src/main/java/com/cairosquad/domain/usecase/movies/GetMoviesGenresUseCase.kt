package com.cairosquad.domain.usecase.movies

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Movie

class GetMoviesGenresUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun getMoviesGenres(page: Int): List<Movie>{
        return moviesRepository.getMoviesGenres(page)
    }
}