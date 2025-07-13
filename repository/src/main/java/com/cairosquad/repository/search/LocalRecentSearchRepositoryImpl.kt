package com.cairosquad.repository.search

import com.cairosquad.domain.search.repository.SearchHistoryRepository
import com.cairosquad.repository.search.data_source.local.LocalRecentSearchDataSource

class LocalRecentSearchRepositoryImpl(
    private val dataSource: LocalRecentSearchDataSource
) : SearchHistoryRepository {

    override suspend fun getAll(): List<String> {
        return dataSource.getAll()
    }

    override suspend fun getByQuery(query: String): List<String> {
        return if (query.isBlank()) {
            dataSource.getAll()
        } else {
            dataSource.getByQuery(query)
        }
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