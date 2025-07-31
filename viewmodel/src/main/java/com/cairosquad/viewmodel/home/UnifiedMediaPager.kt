package com.cairosquad.viewmodel.home

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
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

    fun getCombinedMedia(genreId: Long?): Flow<PagingData<HomeScreenState.MediaUiState>> =
        createPager(
            genreId = genreId,
            getMoviesPage = { page, genre -> manageMoviesUseCase.getPopularMovies(page, genre) },
            getSeriesPage = { page, genre -> manageSeriesUseCase.getPopularSeries(page, genre) })

    fun createPager(
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