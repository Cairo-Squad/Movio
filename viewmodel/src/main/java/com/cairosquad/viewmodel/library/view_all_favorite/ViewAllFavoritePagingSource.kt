package com.cairosquad.viewmodel.library.view_all_favorite

import com.cairosquad.viewmodel.base.BasePagingSource

class ViewAllFavoritePagingSource<T : Any>(
    private val fetcher: suspend (Int) -> List<T>
): BasePagingSource<T>() {
    override suspend fun fetchData(
        page: Int,
        genreId: Long?
    ): List<T> {
        return fetcher(page)
    }
}