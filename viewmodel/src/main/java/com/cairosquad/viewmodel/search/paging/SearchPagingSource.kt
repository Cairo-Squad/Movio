package com.cairosquad.viewmodel.search.paging

import com.cairosquad.viewmodel.base.BasePagingSource
import javax.inject.Inject

class SearchPagingSource<T : Any> @Inject constructor(
    private val query: String,
    private val fetcher: suspend (String, Int) -> List<T>
) : BasePagingSource<T>() {
    override suspend fun fetchData(
        page: Int,
        genreId: Long?
    ): List<T> {
        return fetcher(query, page)
    }
}