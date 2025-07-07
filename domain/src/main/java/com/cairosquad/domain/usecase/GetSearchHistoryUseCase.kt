package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.SearchHistoryRepository
import com.cairosquad.entity.SearchHistoryItem

class GetSearchHistoryUseCase(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend fun getSearchHistory(query: String): List<SearchHistoryItem> {
        TODO()
    }
}