package com.cairosquad.viewmodel.foryou

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cairosquad.domain.usecase.movies.GetPersonalizedMoviesUseCase
import com.cairosquad.entity.Movie
import kotlinx.coroutines.flow.Flow

class ForYouPager(
    private val getPersonalizedMoviesUseCase: GetPersonalizedMoviesUseCase
) {

    fun movies(): Flow<PagingData<Movie>> =
        createPager(getPersonalizedMoviesUseCase::getPersonalizedMovies)

    private fun <T : Any> createPager(
        fetcher: suspend (Int) -> List<T>
    ): Flow<PagingData<T>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 6),
            pagingSourceFactory = { ForYouPagingSource(fetcher) }
        ).flow
    }
}