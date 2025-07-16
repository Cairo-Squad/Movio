package com.cairosquad.viewmodel.search.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cairosquad.domain.search.usecase.SearchUseCase
import com.cairosquad.entity.Series
import kotlinx.coroutines.flow.Flow

class SeriesPagingSource (
    private val searchUseCase: SearchUseCase,
    private val query: String
) : PagingSource<Int, Series>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Series> {
        val page = params.key ?: 1
        return try {
            val movies = searchUseCase.getSeries(query, page)
            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (movies.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Series>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
}
class SeriesPager(private val searchUseCase: SearchUseCase) {
    fun searchMovies(query: String): Flow<PagingData<Series>> =
        Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 2),
            pagingSourceFactory = { SeriesPagingSource(searchUseCase, query) }
        ).flow
}