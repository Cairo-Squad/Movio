package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.SearchHistoryRepository

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
