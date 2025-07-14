package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.MovieDiscoveryRepository
import com.cairosquad.entity.Movie

class GetPersonalizedMoviesUseCase(
    private val movieDiscoveryRepository: MovieDiscoveryRepository
) {
    suspend fun getPersonalizedMovies(): List<Movie> =
        movieDiscoveryRepository.getPersonalizedMovies()
}