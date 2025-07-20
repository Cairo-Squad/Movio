package com.cairosquad.domain.usecase.movies

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Movie

class GetNowPlayingMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun getNowPlayingMovies(page: Int) : List<Movie>{
        return moviesRepository.getNowPlayingMovies(page)
    }
}