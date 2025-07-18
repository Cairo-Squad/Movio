package com.cairosquad.movio.di

import com.cairosquad.viewmodel.search.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    viewModel { (seriesId: Long, seasonNumber: Int) ->
        EpisodesDetailsViewModel(
            seriesDetailsUseCase = get(),
            seriesId = seriesId,
            seasonNumber = seasonNumber
        )
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