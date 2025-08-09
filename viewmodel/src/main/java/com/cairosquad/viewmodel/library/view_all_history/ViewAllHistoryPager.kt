package com.cairosquad.viewmodel.library.view_all_history

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ViewAllHistoryPager @Inject constructor(
    private val accountUseCase: AccountUseCase
) {

    fun movies(): Flow<PagingData<Movie>> {
        return createPager { page ->
            accountUseCase.getHistoryMovies(page)
        }
    }

    fun series(): Flow<PagingData<Series>> {
        return createPager { page ->
            accountUseCase.getHistorySeries(page)
        }
    }

    private fun <T : Any> createPager(
        fetcher: suspend (Int) -> List<T>
    ): Flow<PagingData<T>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 6),
            pagingSourceFactory = { ViewAllHistoryPagingSource(fetcher) }
        ).flow
    }
}