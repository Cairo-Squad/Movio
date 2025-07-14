package com.cairosquad.repository.search

import com.cairosquad.domain.search.repository.SearchHistoryRepository
import com.cairosquad.repository.common.exception.tryToCall
import com.cairosquad.repository.search.data_source.local.LocalRecentSearchDataSource

class LocalRecentSearchRepositoryImpl(
    private val dataSource: LocalRecentSearchDataSource
) : SearchHistoryRepository {

    override suspend fun getAllHistory(): List<String> {
        return tryToCall { dataSource.getAll() }
    }

    override suspend fun getAllHistoryByQuery(query: String): List<String> {
        return tryToCall {
            query.takeIf { it.isNotBlank() }
                ?.let { dataSource.getByQuery(it) }
                ?: dataSource.getAll()
        }
    }

    override suspend fun clearAll() {
        tryToCall { dataSource.clearAll() }
    }

    override suspend fun removeQuery(query: String) {
        tryToCall {dataSource.removeQuery(query)}
    }

    override suspend fun addQuery(query: String) {
        tryToCall { dataSource.addQuery(query) }
    }
}