package com.cairosquad.movio.di

import com.cairosquad.domain.search.repository.SearchRepository
import com.cairosquad.repository.search.SearchRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(get(), get())
    }
}