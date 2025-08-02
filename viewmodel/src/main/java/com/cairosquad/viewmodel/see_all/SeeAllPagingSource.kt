package com.cairosquad.viewmodel.see_all

import com.cairosquad.viewmodel.base.BasePagingSource

class SeeAllPagingSource<T : Any>(
    private val fetcher: suspend (Int, Long?) -> List<T>
) : BasePagingSource<T>() {
    override suspend fun fetchData(
        page: Int,
        genreId: Long?
    ): List<T> {
        return fetcher(page, genreId)
    }
}