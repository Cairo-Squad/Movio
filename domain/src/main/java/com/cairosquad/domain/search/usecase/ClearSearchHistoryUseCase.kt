package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.SearchHistoryRepository

class ClearSearchHistoryUseCase(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend fun clearAll() {
        searchHistoryRepository.clearAll()
    }

    suspend fun removeQuery(query: String) {
        searchHistoryRepository.removeQuery(query)
    }
}
