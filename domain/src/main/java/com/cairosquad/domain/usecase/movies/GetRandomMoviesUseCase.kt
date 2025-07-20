package com.cairosquad.domain.usecase.movies

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Movie

class GetRandomMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun getRandomMovies(page:Int) : List<Movie>{
        return moviesRepository.getRandomMoviesUseCase(page)
    }
}