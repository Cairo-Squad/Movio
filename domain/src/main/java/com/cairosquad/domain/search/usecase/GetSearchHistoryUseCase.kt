package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.SearchHistoryRepository

class GetSearchHistoryUseCase(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend fun getAll(query: String): List<String> {
        TODO()
    }
}