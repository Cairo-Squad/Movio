package datasource

import SearchHistoryDao
import com.cairosquad.local.roomentity.SearchHistoryEntity
import com.cairosquad.repository.dataSource.local.SearchHistoryDataSource

class SearchHistoryDataSourceImpl(
    private val dao: SearchHistoryDao
) : SearchHistoryDataSource{

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
        dao.insertQuery(SearchHistoryEntity(query))
    }

    override suspend fun getAll(): List<String> {
        return dao.getAll().map { it.query }
    }

}