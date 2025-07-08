package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.SearchHistoryRepository

class GetSearchHistoryUseCase(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend fun getAll(query: String): List<String> {
        TODO()
    }
}