import com.cairosquad.repository.dataSource.local.SearchHistoryDataSource

class SearchHistoryDataSourceImpl : SearchHistoryDataSource {
    override suspend fun getByQuery(query: String): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun clearAll() {
        TODO("Not yet implemented")
    }

    override suspend fun removeQuery(query: String) {
        TODO("Not yet implemented")
    }

    override suspend fun addQuery(query: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): List<String> {
        TODO("Not yet implemented")
    }
}