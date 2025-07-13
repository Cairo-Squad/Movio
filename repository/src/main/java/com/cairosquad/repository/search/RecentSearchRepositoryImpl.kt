package com.cairosquad.repository.search

import com.cairosquad.domain.search.repository.RecentSearchRepository
import com.cairosquad.repository.common.exception.tryToCall
import com.cairosquad.repository.search.data_source.local.RecentSearchDataSource

class RecentSearchRepositoryImpl(
    private val dataSource: RecentSearchDataSource
) : RecentSearchRepository {

    override suspend fun getAll(): List<String> {
        return tryToCall {
            dataSource.getAll()
        }
    }

    override suspend fun getByQuery(query: String): List<String> {
        return tryToCall {
            if (query.isBlank()) {
                emptyList()
            } else {
                dataSource.getByQuery(query)
            }
        }
    }


    override suspend fun clearAll() {
        tryToCall {
            dataSource.clearAll()
        }
    }

    override suspend fun removeQuery(query: String) {
        tryToCall {
            dataSource.removeQuery(query)
        }
    }

    override suspend fun addQuery(query: String) {
        tryToCall {
            dataSource.addQuery(query)
        }
    }
}