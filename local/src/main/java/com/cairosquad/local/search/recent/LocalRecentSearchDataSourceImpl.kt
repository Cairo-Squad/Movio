package com.cairosquad.local.search.recent

import com.cairosquad.local.search.recent.dao.LocalRecentSearchDao
import com.cairosquad.local.search.recent.entity.RecentSearchEntity
import com.cairosquad.repository.search.data_source.local.LocalRecentSearchDataSource

class LocalRecentSearchDataSourceImpl(
    private val dao: LocalRecentSearchDao
) : LocalRecentSearchDataSource {

    override suspend fun getByQuery(query: String): List<String> {
        return dao.getAllQueries(query).map { it.query }
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
        return dao.getAllQueries().map { it.query }
    }

}