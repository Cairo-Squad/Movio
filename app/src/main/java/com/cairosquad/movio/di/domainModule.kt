package com.cairosquad.movio.di

import com.cairosquad.domain.search.usecase.ClearSearchHistoryUseCase
import com.cairosquad.domain.search.usecase.GetSuggestedMoviesUseCase
import com.cairosquad.domain.search.usecase.GetPersonalizedMoviesUseCase
import com.cairosquad.domain.search.usecase.GetLocalSearchHistoryUseCase
import com.cairosquad.domain.search.usecase.SearchUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::SearchUseCase)
    singleOf(::GetPersonalizedMoviesUseCase)
    singleOf(::GetSuggestedMoviesUseCase)
    singleOf(::GetLocalSearchHistoryUseCase)
    singleOf(::ClearSearchHistoryUseCase)
}