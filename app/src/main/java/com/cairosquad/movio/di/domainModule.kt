package com.cairosquad.movio.di

import com.cairosquad.domain.search.usecase.GetExploreMoreUseCase
import com.cairosquad.domain.search.usecase.GetForYouUseCase
import com.cairosquad.domain.search.usecase.SearchUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::SearchUseCase)
    singleOf(::GetForYouUseCase)
    singleOf(::GetExploreMoreUseCase)
}