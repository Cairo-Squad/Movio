package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.DiscoveryRepository
import com.cairosquad.entity.Movie

class GetPersonalizedMoviesUseCase(
    private val discoveryRepository: DiscoveryRepository
) {
    suspend fun getPersonalizedMovies(): List<Movie> =
        discoveryRepository.getPersonalizedMovies()
}