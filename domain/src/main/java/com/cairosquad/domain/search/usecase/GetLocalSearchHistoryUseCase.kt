package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.SearchHistoryRepository

class GetLocalSearchHistoryUseCase(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend fun getAll() = searchHistoryRepository.getAll()
    suspend fun getByQuery(query: String) = searchHistoryRepository.getByQuery(query)
}
