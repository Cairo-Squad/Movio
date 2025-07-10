package com.cairosquad.domain.search.repository


interface SearchHistoryRepository {
    suspend fun getAll(query: String): List<String>

    suspend fun clearAll()

    suspend fun removeQuery(query: String)

    suspend fun addQuery(query: String)
}