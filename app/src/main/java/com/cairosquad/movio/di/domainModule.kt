package com.cairosquad.movio.di

import com.cairosquad.domain.search.usecase.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::SearchUseCase)
    singleOf(::GetPersonalizedMoviesUseCase)
    singleOf(::GetSuggestedMoviesUseCase)
    singleOf(::GetLocalSearchHistoryUseCase)
    singleOf(::ClearSearchHistoryUseCase)
    singleOf(::GetArtistDetailsUseCase)
    singleOf(::GetMoviesDetailsUseCase)
    singleOf(::GetSeriesDetailsUseCase)
}