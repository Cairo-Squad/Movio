package com.cairosquad.viewmodel.library.view_all_history

import com.cairosquad.viewmodel.base.BasePagingSource

class ViewAllHistoryPagingSource<T : Any>(
    private val fetcher: suspend (Int) -> List<T>
): BasePagingSource<T>() {
    override suspend fun fetchData(
        page: Int,
        genreId: Long?
    ): List<T> {
        return fetcher(page)
    }
}