package com.cairosquad.domain.usecase.movies

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Movie

class GetFreeToWatchMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun getFreeToWatchMovies(page : Int): List<Movie> {
       return moviesRepository.getFreeToWatchMovies(page)
    }
}