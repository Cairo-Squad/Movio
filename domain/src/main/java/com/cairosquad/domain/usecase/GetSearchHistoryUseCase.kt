package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.SearchHistoryRepository

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
