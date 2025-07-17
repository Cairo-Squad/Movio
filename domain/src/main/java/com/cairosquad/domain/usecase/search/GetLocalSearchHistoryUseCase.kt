package com.cairosquad.domain.usecase.search

import com.cairosquad.domain.repository.SearchRepository

class GetLocalSearchHistoryUseCase(
    private val searchRepository: SearchRepository
) {
    suspend fun getAll() = searchRepository.getAllHistory()
    suspend fun getByQuery(query: String) = searchRepository.getAllHistoryByQuery(query)
}
