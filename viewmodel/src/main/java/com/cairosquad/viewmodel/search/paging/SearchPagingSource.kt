package com.cairosquad.viewmodel.search.paging

import com.cairosquad.viewmodel.base.BasePagingSource

class SearchPagingSource<T : Any>(
    private val query: String,
    private val fetcher: suspend (String, Int) -> List<T>
) : BasePagingSource<T>() {
    override suspend fun fetchData(page: Int): List<T> {
        return fetcher(query, page)
    }
}