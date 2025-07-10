package com.cairosquad.repository.dataSource

import com.cairosquad.domain.search.repository.RecentSearchRepository
import com.cairosquad.repository.dataSource.local.SearchHistoryDataSource

class RecentSearchRepositoryImpl(
    private val dataSource: SearchHistoryDataSource
) : RecentSearchRepository {

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