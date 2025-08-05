package com.cairosquad.repository.search

import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.repository.search.data_source.local.LocalRecentSearchDataSource
import com.cairosquad.repository.utils.mappers.tryToCall
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val localRecentSearchDataSource: LocalRecentSearchDataSource
) : SearchRepository {
    override suspend fun getAllHistory(): List<String> {
        return tryToCall { localRecentSearchDataSource.getAll() }
    }

    override suspend fun getAllHistoryByQuery(query: String): List<String> {
        return tryToCall {
            query.takeIf { it.isNotBlank() }
                ?.let { query ->
                    localRecentSearchDataSource.getByQuery(query)
                        .distinct().shuffled().take(20)
                }
                ?: localRecentSearchDataSource.getAll()
        }
    }

    override suspend fun clearAll() {
        tryToCall { localRecentSearchDataSource.clearAll() }
    }

    override suspend fun removeQuery(query: String) {
        tryToCall { localRecentSearchDataSource.removeQuery(query) }
    }

    override suspend fun addQuery(query: String) {
        tryToCall { localRecentSearchDataSource.addQuery(query) }
    }
}