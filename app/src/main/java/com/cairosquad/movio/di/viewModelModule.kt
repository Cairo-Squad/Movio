package com.cairosquad.movio.di

import com.cairosquad.viewmodel.details.artist.ArtistViewModel
import com.cairosquad.viewmodel.details.movie.MovieViewModel
import com.cairosquad.viewmodel.details.reviews.ReviewsViewModel
import com.cairosquad.viewmodel.details.series.SeriesDetailsViewModel
import com.cairosquad.viewmodel.details.similar_movies.SimilarMoviesViewModel
import com.cairosquad.viewmodel.details.top_cast.TopCastViewModel
import com.cairosquad.viewmodel.search.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    viewModelOf(::SimilarMoviesViewModel)
    viewModel { (movieId: Long) ->

        MovieViewModel(movieId = movieId, movieUseCase = get())
    }

    viewModel { (seriesId: Long) ->
        SeriesDetailsViewModel(seriesDetailsUseCase = get(), seriesId = seriesId)
    }
    viewModel { (mediaId: Long, isMovie: Boolean) ->
        TopCastViewModel(
            mediaId = mediaId,
            isMovie = isMovie,
            getMoviesDetailsUseCase = get(),
            getSeriesDetailsUseCase = get()
        )
    }

    viewModel { (mediaId: Long, isMovie: Boolean) ->
        ReviewsViewModel(
            mediaId = mediaId,
            isMovie = isMovie,
            getMoviesDetailsUseCase = get(),
            getSeriesDetailsUseCase = get()
        )
    }

    viewModelOf(::ArtistViewModel)
}