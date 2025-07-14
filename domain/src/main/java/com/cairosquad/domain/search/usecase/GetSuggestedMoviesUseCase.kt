package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.MovieDiscoveryRepository
import com.cairosquad.entity.Movie

class GetSuggestedMoviesUseCase(private val movieDiscoveryRepository: MovieDiscoveryRepository) {
    suspend fun getSuggestedMovies(): List<Movie> =
        movieDiscoveryRepository.getSuggestedMovies()
}