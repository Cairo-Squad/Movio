package com.cairosquad.viewmodel.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

class UnifiedMediaPagingSource(
   private val genreId: Long? = null,
    private val getMoviesPage: suspend (Int,Long?) -> List<Movie>,
    private val getSeriesPage: suspend (Int,Long?) -> List<Series>,
) : PagingSource<Int, HomeScreenState.MediaUiState>() {
    override fun getRefreshKey(state: PagingState<Int, HomeScreenState.MediaUiState>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomeScreenState.MediaUiState> {
        val page = params.key ?: 1

        return try {
            val movies = getMoviesPage(page,genreId).take(10).map{it.toUiState()}
            val series = getSeriesPage(page,genreId).take(10).map{it.toUiState()}

            val combined = (movies + series).shuffled()

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