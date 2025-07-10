package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.RecommendationRepository
import com.cairosquad.entity.Movie

class GetExploreMoreUseCase(private val recommendationRepository: RecommendationRepository) {
    suspend fun getExploreMoreMovies(): List<Movie> =
        recommendationRepository.getExploreMoreMovies()
}