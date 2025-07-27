package com.cairosquad.viewmodel.search.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cairosquad.domain.usecase.ManageArtistUseCase
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import kotlinx.coroutines.flow.Flow

class SearchPager(
    private val manageMoviesUseCase: ManageMoviesUseCase,
    private val manageSeriesUseCase: ManageSeriesUseCase,
    private val manageArtistUseCase: ManageArtistUseCase
) {

    fun movies(query: String): Flow<PagingData<Movie>> = createPager(query, manageMoviesUseCase::getMoviesByQuery)
    fun series(query: String): Flow<PagingData<Series>> = createPager(query, manageSeriesUseCase::getSeriesByQuery)
    fun artists(query: String): Flow<PagingData<Artist>> = createPager(query, manageArtistUseCase::getArtistsByQuery)

    private fun <T : Any> createPager(
        query: String,
        fetcher: suspend (String, Int) -> List<T>
    ): Flow<PagingData<T>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 2),
            pagingSourceFactory = { SearchPagingSource(query, fetcher) }
        ).flow
    }
}