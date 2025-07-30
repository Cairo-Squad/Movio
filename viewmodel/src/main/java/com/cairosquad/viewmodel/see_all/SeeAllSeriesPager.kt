package com.cairosquad.viewmodel.see_all

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Series
import kotlinx.coroutines.flow.Flow

class SeeAllSeriesPager(
    private val manageSeriesUseCase: ManageSeriesUseCase
) {
    fun getTopRatingSeries(genreId: Long?): Flow<PagingData<Series>> =
        createPager(genreId) { page, genre -> manageSeriesUseCase.getTopRatingSeries(page, genre) }

    fun getMoreRecommendedSeries(genreId: Long?): Flow<PagingData<Series>> =
        createPager(genreId) { page, genre ->
            manageSeriesUseCase.getMoreRecommendedSeries(
                page,
                genre
            )
        }

    fun getAiringTodaySeries(genreId: Long?): Flow<PagingData<Series>> =
        createPager(genreId) { page, genre ->
            manageSeriesUseCase.getAiringTodaySeries(
                page,
                genre
            )
        }

    fun getOnTvSeries(genreId: Long?): Flow<PagingData<Series>> =
        createPager(genreId) { page, genre ->
            manageSeriesUseCase.getOnTvSeries(
                page,
                genre
            )
        }

    fun getTrendingSeries(genreId: Long?): Flow<PagingData<Series>> =
        createPager(genreId) { page, genre -> manageSeriesUseCase.getTrendingSeries(page, genre) }

    fun getFreeToWatchSeries(genreId: Long?): Flow<PagingData<Series>> =
        createPager(genreId) { page, genre ->
            manageSeriesUseCase.getFreeToWatchSeries(
                page,
                genre
            )
        }


    private fun <T : Any> createPager(
        genreId: Long? = null,
        fetcher: suspend (Int, Long?) -> List<T>
    ): Flow<PagingData<T>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 6),
            pagingSourceFactory = {
                SeeAllPagingSource { page, _ -> fetcher(page, genreId) }
            }
        ).flow
    }
}