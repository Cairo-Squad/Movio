package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.RecentSearchRepository

class ClearRecentSearchUseCase(
    private val recentSearchRepository: RecentSearchRepository
) {
    suspend fun clearAll() {
        recentSearchRepository.clearAll()
    }

    suspend fun removeQuery(query: String) {
        recentSearchRepository.removeQuery(query)
    }
}
