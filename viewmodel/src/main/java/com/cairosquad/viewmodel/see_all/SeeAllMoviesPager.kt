package com.cairosquad.viewmodel.see_all

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.entity.Movie
import kotlinx.coroutines.flow.Flow

class SeeAllMoviesPager(
    private val manageMoviesUseCase: ManageMoviesUseCase
) {
    fun getTopRatingMovies(genreId: Long?): Flow<PagingData<Movie>> =
        createPager(genreId) { page, genre ->
            manageMoviesUseCase.getTopRatingMovies(page, genre)
        }

    fun getMoreRecommendedMovies(genreId: Long?): Flow<PagingData<Movie>> =
        createPager(genreId) { page, genre ->
            manageMoviesUseCase.getMoreRecommendedMovies(
                page,
                genre
            )
        }

    fun getUpcomingMovies(genreId: Long?): Flow<PagingData<Movie>> =
        createPager(genreId) { page, genre -> manageMoviesUseCase.getUpcomingMovies(page, genre) }

    fun getFreeToWatchMovies(genreId: Long?): Flow<PagingData<Movie>> =
        createPager(genreId) { page, genre ->
            manageMoviesUseCase.getFreeToWatchMovies(
                page,
                genre
            )
        }

    fun getTrendingMovies(genreId: Long?): Flow<PagingData<Movie>> =
        createPager(genreId) { page, genre -> manageMoviesUseCase.getTrendingMovies(page, genre) }

    fun getNowPlayingMovies(genreId: Long?): Flow<PagingData<Movie>> =
        createPager(genreId) { page, genre -> manageMoviesUseCase.getNowPlayingMovies(page, genre) }


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