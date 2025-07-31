package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.SearchRecommendationRepository
import javax.inject.Inject

class GetSearchRecommendationUseCase @Inject constructor(
    private val searchRecommendationRepository: SearchRecommendationRepository
) {
    suspend operator fun invoke(query: String): List<String> {
        return searchRecommendationRepository.getSearchRecommendation(query)
    }
}
