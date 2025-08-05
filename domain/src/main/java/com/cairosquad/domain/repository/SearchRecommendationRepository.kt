package com.cairosquad.domain.repository

interface SearchRecommendationRepository {
    suspend fun getSearchRecommendation(query: String): List<String>

}