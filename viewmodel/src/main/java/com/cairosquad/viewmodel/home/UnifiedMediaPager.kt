package com.cairosquad.viewmodel.home

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cairosquad.domain.model.SortType
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UnifiedMediaPager @Inject constructor(
    private val manageMoviesUseCase: ManageMoviesUseCase,
    private val manageSeriesUseCase: ManageSeriesUseCase
) {
    fun getCombinedMedia(genreId: Long?, sortingType: SortType? = null): Flow<PagingData<HomeScreenState.MediaUiState>> {
        return createPager(
            genreId = genreId,
            getMoviesPage = { page, genre -> manageMoviesUseCase.getAllMovies(page, genre, sortingType) },
            getSeriesPage = { page, genre -> manageSeriesUseCase.getAllSeries(page, genre, sortingType) })
    }

    private fun createPager(
        genreId: Long? = null,
        getMoviesPage: suspend (page: Int, genreId: Long?) -> List<Movie>,
        getSeriesPage: suspend (page: Int, genreId: Long?) -> List<Series>,
    ): Flow<PagingData<HomeScreenState.MediaUiState>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 6),
            pagingSourceFactory = {
                UnifiedMediaPagingSource(
                    genreId = genreId,
                    getMoviesPage = getMoviesPage,
                    getSeriesPage = getSeriesPage,

                    )
            }
        ).flow
    }
}