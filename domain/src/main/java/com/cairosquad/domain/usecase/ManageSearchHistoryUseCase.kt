package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.SearchRepository

class ManageSearchHistoryUseCase(
    private val searchRepository: SearchRepository
) {
    suspend fun clearAllHistory() {
        searchRepository.clearAll()
    }

    suspend fun removeQueryFromHistory(query: String) {
        searchRepository.removeQuery(query)
    }

    suspend fun getAll() = searchRepository.getAllHistory()

    suspend fun getByQuery(query: String) = searchRepository.getAllHistoryByQuery(query)
}
