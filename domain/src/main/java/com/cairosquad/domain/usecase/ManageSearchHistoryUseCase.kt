package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.SearchRepository
import javax.inject.Inject

class ManageSearchHistoryUseCase @Inject constructor(
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
