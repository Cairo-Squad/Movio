package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.RecommendationRepository
import com.cairosquad.entity.Movie

class GetForYouUseCase(private val recommendationRepository: RecommendationRepository) {
    suspend fun getForYouMovies(): List<Movie> = recommendationRepository.getForYouMovies()
}