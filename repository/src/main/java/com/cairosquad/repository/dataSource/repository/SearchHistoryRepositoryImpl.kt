package com.cairosquad.repository.dataSource.repository

import com.cairosquad.domain.repository.SearchHistoryRepository
import com.cairosquad.repository.dataSource.local.SearchHistoryDataSource

class SearchHistoryRepositoryImpl(
    private val localDataSource: SearchHistoryDataSource
) : SearchHistoryRepository {


    override suspend fun getAll(): List<String> {
        return localDataSource.getAllQueries()
    }

    override suspend fun getByQuery(query: String): List<String> {
        return if (query.isBlank()) {
            localDataSource.getAllQueries()
        } else {
            localDataSource.getFilteredQueries(query)
        }
    }

    override suspend fun clearAll() {
        localDataSource.clearAll()
    }

    override suspend fun removeQuery(query: String) {
        localDataSource.removeQuery(query)
    }

    override suspend fun addQuery(query: String) {
        localDataSource.addQuery(query)
    }
}