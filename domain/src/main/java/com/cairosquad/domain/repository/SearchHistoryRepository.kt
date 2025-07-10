package com.cairosquad.domain.repository


interface SearchHistoryRepository {
    suspend fun getAll(): List<String>

    suspend fun getByQuery(query: String): List<String>
    suspend fun clearAll()

    suspend fun removeQuery(query: String)

    suspend fun addQuery(query: String)
}