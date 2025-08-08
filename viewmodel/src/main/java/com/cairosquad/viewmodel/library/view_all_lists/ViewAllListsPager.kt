package com.cairosquad.viewmodel.library.view_all_lists

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.entity.MediaList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ViewAllListsPager @Inject constructor(
    private val accountUseCase: AccountUseCase
) {

    fun movies(): Flow<PagingData<MediaList>> {
        return createPager { page ->
            accountUseCase.getMoviesLists(page)
        }
    }

    fun series(): Flow<PagingData<MediaList>> {
        return createPager { page ->
            accountUseCase.getSeriesLists(page)
        }
    }

    private fun <T : Any> createPager(
        fetcher: suspend (Int) -> List<T>
    ): Flow<PagingData<T>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 6),
            pagingSourceFactory = { ViewAllListsSource(fetcher) }
        ).flow
    }
}