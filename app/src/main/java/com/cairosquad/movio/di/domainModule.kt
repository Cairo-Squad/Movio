package com.cairosquad.movio.di

import com.cairosquad.domain.search.usecase.GetExploreMoreUseCase
import com.cairosquad.domain.search.usecase.GetForYouUseCase
import com.cairosquad.domain.search.usecase.SearchUseCase
import org.koin.dsl.module

val domainModule = module {
    single {
        SearchUseCase(get(), get())
    }

    single {
        GetForYouUseCase(get())
    }

    single {
        GetExploreMoreUseCase(get())
    }
}