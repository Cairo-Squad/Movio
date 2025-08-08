package com.cairosquad.viewmodel.library.view_all_favorite

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ViewAllFavoritePager @Inject constructor(
    private val accountUseCase: AccountUseCase
) {

    fun movies(): Flow<PagingData<Movie>> {
        return createPager { page ->
            accountUseCase.getFavoriteMovies(page)
        }
    }

    fun series(): Flow<PagingData<Series>> {
        return createPager { page ->
            accountUseCase.getFavoriteSeries(page)
        }
    }

    private fun <T : Any> createPager(
        fetcher: suspend (Int) -> List<T>
    ): Flow<PagingData<T>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 6),
            pagingSourceFactory = { ViewAllFavoritePagingSource(fetcher) }
        ).flow
    }
}