package com.cairosquad.viewmodel.library.list_content

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListContentPager @Inject constructor(
    private val accountUseCase: AccountUseCase
) {

    fun movies(listId: Long): Flow<PagingData<Movie>> {
        return createPager { page ->
            accountUseCase.getMoviesOfList(listId, page)
        }
    }

    fun series(listId: Long): Flow<PagingData<Series>> {
        return createPager { page ->
            accountUseCase.getSeriesOfList(listId, page)
        }
    }

    private fun <T : Any> createPager(
        fetcher: suspend (Int) -> List<T>
    ): Flow<PagingData<T>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 6),
            pagingSourceFactory = { ListContentPagingSource(fetcher) }
        ).flow
    }
}