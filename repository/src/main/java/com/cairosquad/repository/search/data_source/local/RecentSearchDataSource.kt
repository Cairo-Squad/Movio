package com.cairosquad.repository.search.data_source.local

interface RecentSearchDataSource {
    suspend fun getByQuery(query: String): List<String>
    suspend fun clearAll()
    suspend fun removeQuery(query: String)
    suspend fun addQuery(query: String)
    suspend fun getAll(): List<String>
}