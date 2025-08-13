package com.cairosquad.viewmodel.rated.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cairosquad.domain.usecase.GetRatedItemsUseCase
import com.cairosquad.viewmodel.rated.RatedItemUiState
import com.cairosquad.viewmodel.rated.mappers.toRatedItemUiState

class RatedItemsPagingSource(
    private val getRatedItemsUseCase: GetRatedItemsUseCase
) : PagingSource<Int, RatedItemUiState>() {

    override fun getRefreshKey(state: PagingState<Int, RatedItemUiState>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RatedItemUiState> {
        val page = params.key ?: 1

        return try {
            val movies = getRatedItemsUseCase.getRatedMovies(page)
            val series = getRatedItemsUseCase.getRatedSeries(page)
            val movieItems = movies.map { (movie, _) -> movie.toRatedItemUiState() }
            val seriesItems = series.map { (series,_) -> series.toRatedItemUiState() }

            val combined = (movieItems + seriesItems).sortedByDescending { it.rating }

            LoadResult.Page(
                data = combined,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (combined.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
