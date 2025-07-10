package com.cairosquad.local.search.recent

import com.cairosquad.local.search.recent.dao.RecentSearchDao
import com.cairosquad.local.search.recent.entity.RecentSearchEntity
import com.cairosquad.repository.dataSource.local.SearchHistoryDataSource

class SearchHistoryDataSourceImpl(
    private val dao: RecentSearchDao
) : SearchHistoryDataSource {

    override suspend fun getByQuery(query: String): List<String> {
        return dao.getAll(query).map { it.query }
    }

    override suspend fun clearAll() {
        dao.clearAll()
    }

    override suspend fun removeQuery(query: String) {
        dao.deleteQuery(query)
    }

    override suspend fun addQuery(query: String) {
        dao.insertQuery(RecentSearchEntity(query))
    }

    override suspend fun getAll(): List<String> {
        return dao.getAll().map { it.query }
    }

}