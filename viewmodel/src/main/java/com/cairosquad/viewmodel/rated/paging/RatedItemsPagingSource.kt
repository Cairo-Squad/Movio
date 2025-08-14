package com.cairosquad.viewmodel.rated.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cairosquad.domain.usecase.GetRatedItemsUseCase
import com.cairosquad.viewmodel.rated.MyRatingsScreenState
import com.cairosquad.viewmodel.rated.mappers.toRatedItemUiState

class RatedItemsPagingSource(
    private val getRatedItemsUseCase: GetRatedItemsUseCase
) : PagingSource<Int, MyRatingsScreenState.RatedItemUiState>() {

    override fun getRefreshKey(state: PagingState<Int, MyRatingsScreenState.RatedItemUiState>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MyRatingsScreenState.RatedItemUiState> {
        val page = params.key ?: 1

        return try {
            val movies = getRatedItemsUseCase.getRatedMovies(page)
            val series = getRatedItemsUseCase.getRatedSeries(page)
            val movieItems = movies.map { (movie, rating) -> movie.toRatedItemUiState(rating) }
            val seriesItems = series.map { (series,rating) -> series.toRatedItemUiState(rating) }

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
