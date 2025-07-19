package com.cairosquad.viewmodel.foryou

import com.cairosquad.viewmodel.base.BasePagingSource

class ForYouPagingSource<T : Any>(
    private val fetcher: suspend (Int) -> List<T>
) : BasePagingSource<T>() {
    override suspend fun fetchData(page: Int): List<T> {
        return fetcher(page)
    }
}