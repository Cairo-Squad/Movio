package com.cairosquad.movio.di

import com.cairosquad.viewmodel.details.series.SeriesDetailsViewModel
import com.cairosquad.viewmodel.details.series.season.SeasonViewModel
import com.cairosquad.viewmodel.search.SearchViewModel
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    viewModel { (seriesId: Long) ->
        SeriesDetailsViewModel(seriesDetailsUseCase = get(), seriesId = seriesId)
    }

    viewModel { (seriesId: Long, seasonNumber: Int, dispatcher: CoroutineDispatcher) ->
        SeasonViewModel(
            seriesDetailsUseCase = get(),
            dispatcher = dispatcher,
            seriesId = seriesId,
            seasonNumber = seasonNumber
        )
    }

}