package com.cairosquad.movio.di

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.repository.movies.MoviesRepositoryImpl
import com.cairosquad.repository.search.SearchRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(get(), get(),get())
    }

    single<MoviesRepository> {
        MoviesRepositoryImpl(get(), get())
    }
}