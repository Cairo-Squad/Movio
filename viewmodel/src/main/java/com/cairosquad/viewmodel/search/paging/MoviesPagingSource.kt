package com.cairosquad.viewmodel.search.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cairosquad.domain.search.usecase.SearchUseCase
import com.cairosquad.entity.Movie
import kotlinx.coroutines.flow.Flow

class MoviesPagingSource(
    private val searchUseCase: SearchUseCase,
    private val query: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val movies = searchUseCase.getMovies(query, page)
            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (movies.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
}

class MoviePager(private val searchUseCase: SearchUseCase) {
    fun searchMovies(query: String): Flow<PagingData<Movie>> {
       return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 2),
            pagingSourceFactory = { MoviesPagingSource(searchUseCase, query) }
        ).flow
    }
}