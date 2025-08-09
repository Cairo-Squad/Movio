package com.cairosquad.viewmodel.library.view_all_lists

import com.cairosquad.viewmodel.base.BasePagingSource

class ViewAllListsSource<T : Any>(
    private val fetcher: suspend (Int) -> List<T>
): BasePagingSource<T>() {
    override suspend fun fetchData(
        page: Int,
        genreId: Long?
    ): List<T> {
        return fetcher(page)
    }
}