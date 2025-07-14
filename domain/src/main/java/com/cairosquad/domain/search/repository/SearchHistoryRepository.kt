package com.cairosquad.domain.search.repository


interface SearchHistoryRepository {
    suspend fun getAllHistory(): List<String>

    suspend fun getAllHistoryByQuery(query: String): List<String>

    suspend fun clearAll()

    suspend fun removeQuery(query: String)

    suspend fun addQuery(query: String)
}