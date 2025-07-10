package com.cairosquad.repository.dataSource.local

interface SearchHistoryDataSource {
    suspend fun getByQuery(query: String): List<String>
    suspend fun clearAll()
    suspend fun removeQuery(query: String)
    suspend fun addQuery(query: String)
    suspend fun getAll(): List<String>
}
