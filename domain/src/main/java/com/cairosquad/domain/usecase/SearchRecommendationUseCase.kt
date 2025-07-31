package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.SearchRecommendationRepository

class GetSearchRecommendationUseCase(
    private val searchRecommendationRepository: SearchRecommendationRepository
) {
    suspend operator fun invoke(query: String): List<String> {
        return searchRecommendationRepository.getSearchRecommendation(query)
    }
}
