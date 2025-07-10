package com.cairosquad.repository.dataSource

import com.cairosquad.domain.repository.SearchHistoryRepository
import com.cairosquad.repository.dataSource.local.SearchHistoryDataSource

class SearchHistoryRepositoryImpl(
    private val dataSource: SearchHistoryDataSource
) : SearchHistoryRepository {

    override suspend fun getAll(query: String): List<String> {
        return dataSource.getAll(query)
    }

    override suspend fun clearAll() {
        dataSource.clearAll()
    }

    override suspend fun removeQuery(query: String) {
        dataSource.removeQuery(query)
    }

    override suspend fun addQuery(query: String) {
        dataSource.addQuery(query)
    }
}