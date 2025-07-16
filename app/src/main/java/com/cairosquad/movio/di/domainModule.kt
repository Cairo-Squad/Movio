package com.cairosquad.movio.di

import com.cairosquad.domain.usecase.search.ClearSearchHistoryUseCase
import com.cairosquad.domain.usecase.movies.GetSuggestedMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetPersonalizedMoviesUseCase
import com.cairosquad.domain.usecase.search.GetLocalSearchHistoryUseCase
import com.cairosquad.domain.usecase.search.SearchUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::SearchUseCase)
    singleOf(::GetPersonalizedMoviesUseCase)
    singleOf(::GetSuggestedMoviesUseCase)
    singleOf(::GetLocalSearchHistoryUseCase)
    singleOf(::ClearSearchHistoryUseCase)
}