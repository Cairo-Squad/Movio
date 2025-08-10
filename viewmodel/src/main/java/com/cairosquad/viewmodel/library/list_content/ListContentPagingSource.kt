package com.cairosquad.viewmodel.library.list_content

import com.cairosquad.viewmodel.base.BasePagingSource

class ListContentPagingSource<T : Any>(
    private val fetcher: suspend (Int) -> List<T>
): BasePagingSource<T>() {
    override suspend fun fetchData(
        page: Int,
        genreId: Long?
    ): List<T> {
        return fetcher(page)
    }

}