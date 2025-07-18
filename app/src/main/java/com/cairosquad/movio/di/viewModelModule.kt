package com.cairosquad.movio.di

import com.cairosquad.viewmodel.details.reviews.ReviewsViewModel
import com.cairosquad.viewmodel.details.artist.ArtistViewModel
import com.cairosquad.viewmodel.details.top_cast.TopCastViewModel
import com.cairosquad.viewmodel.details.series.SeriesDetailsViewModel
import com.cairosquad.viewmodel.details.series.season.SeasonViewModel
import com.cairosquad.viewmodel.search.SearchViewModel
import org.koin.core.module.dsl.viewModel
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
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
    viewModel { (seriesId: Long) ->
        SeriesDetailsViewModel(seriesDetailsUseCase = get(), seriesId = seriesId)
    }

    viewModel { (seriesId: Long, seasonNumber: Int) ->
        SeasonViewModel(
            seriesDetailsUseCase = get(),
            seriesId = seriesId,
            seasonNumber = seasonNumber
        )
    }

}