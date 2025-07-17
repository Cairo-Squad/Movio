package com.cairosquad.viewmodel.search.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cairosquad.domain.search.usecase.SearchUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import kotlinx.coroutines.flow.Flow

class SearchPager(
    private val searchUseCase: SearchUseCase
) {

    fun movies(query: String): Flow<PagingData<Movie>> = createPager(query, searchUseCase::getMovies)
    fun series(query: String): Flow<PagingData<Series>> = createPager(query, searchUseCase::getSeries)
    fun artists(query: String): Flow<PagingData<Artist>> = createPager(query, searchUseCase::getArtists)

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