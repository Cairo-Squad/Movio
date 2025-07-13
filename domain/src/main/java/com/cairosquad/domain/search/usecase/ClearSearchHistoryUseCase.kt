package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.SearchHistoryRepository

class ClearSearchHistoryUseCase(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend fun clearAllHistory() {
        searchHistoryRepository.clearAll()
    }

    suspend fun removeQueryFromHistory(query: String) {
        searchHistoryRepository.removeQuery(query)
    }
}
