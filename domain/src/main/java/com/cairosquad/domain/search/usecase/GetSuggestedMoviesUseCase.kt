package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.DiscoveryRepository
import com.cairosquad.entity.Movie

class GetSuggestedMoviesUseCase(private val discoveryRepository: DiscoveryRepository) {
    suspend fun getSuggestedMovies(): List<Movie> =
        discoveryRepository.getSuggestedMovies()
}