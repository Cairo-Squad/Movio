//package com.cairosquad.local.datasource
//
//import SearchHistoryDao
//import com.cairosquad.local.roomentity.SearchHistoryEntity
//
//class SearchHistoryDataSource(
//    private val dao: SearchHistoryDao
//) {
//
//    suspend fun getAll(query: String): List<String> {
//        return dao.getAll(query).map { it.query }
//    }
//
//    suspend fun clearAll() {
//        dao.clearAll()
//    }
//
//    suspend fun removeQuery(query: String) {
//        dao.deleteQuery(query)
//    }
//
//    suspend fun addQuery(query: String) {
//        dao.insertQuery(SearchHistoryEntity(query))
//    }
//}