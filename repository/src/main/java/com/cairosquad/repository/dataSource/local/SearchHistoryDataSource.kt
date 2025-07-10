package com.cairosquad.repository.dataSource.local

interface SearchHistoryDataSource {


    suspend fun addQuery(query: String)

    suspend fun getAllQueries(): List<String>


    suspend fun getFilteredQueries(query: String): List<String>

    suspend fun removeQuery(query: String)

    suspend fun clearAll()
}