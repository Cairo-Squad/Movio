package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.SearchHistoryRepository

class GetSearchHistoryUseCase(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend fun getAll(): List<String> {
        return searchHistoryRepository.getAll()
    }
    suspend fun  getByQuery(query: String): List<String> {
        return searchHistoryRepository.getByQuery(query)
    }
}
