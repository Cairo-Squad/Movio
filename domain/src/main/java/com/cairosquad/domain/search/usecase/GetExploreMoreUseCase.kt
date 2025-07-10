package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.MovieRepository
import com.cairosquad.entity.Movie

class GetExploreMoreUseCase(  private val movieRepository: MovieRepository,) {
    suspend fun getExploreMoreMovies(): List<Movie> = movieRepository.getExploreMoreMovies()
}