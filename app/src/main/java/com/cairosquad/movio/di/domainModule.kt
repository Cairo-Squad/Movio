package com.cairosquad.movio.di

import com.cairosquad.domain.search.usecase.SearchUseCase
import org.koin.dsl.module

val domainModule = module {
    single {
        SearchUseCase(get())
    }
}