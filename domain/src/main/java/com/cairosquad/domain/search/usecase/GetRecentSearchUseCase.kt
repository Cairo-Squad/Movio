package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.RecentSearchRepository

class GetRecentSearchUseCase(
    private val recentSearchRepository: RecentSearchRepository
) {
    suspend fun getAll() = recentSearchRepository.getAll()
    suspend fun getByQuery(query: String) = recentSearchRepository.getByQuery(query)
}
