package com.cairosquad.domain.usecase.search

import com.cairosquad.domain.repository.SearchRepository

class ClearSearchHistoryUseCase(
    private val searchRepository: SearchRepository
) {
    suspend fun clearAllHistory() {
        searchRepository.clearAll()
    }

    suspend fun removeQueryFromHistory(query: String) {
        searchRepository.removeQuery(query)
    }
}
