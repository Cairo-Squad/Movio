package com.cairosquad.domain.repository

import com.cairosquad.entity.SearchHistoryItem

interface SearchHistoryRepository {
    suspend fun getSearchHistory(query: String): List<SearchHistoryItem>

    suspend fun clearAllSearchHistory()

    suspend fun clearSearchHistoryById(id: Long)

    suspend fun addSearchHistoryItem(item: SearchHistoryItem)
}