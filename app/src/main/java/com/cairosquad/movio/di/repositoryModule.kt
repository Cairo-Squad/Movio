package com.cairosquad.movio.di

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.repository.search.MoviesRepositoryImpl
import com.cairosquad.repository.search.SearchRepositoryImpl
import com.cairosquad.viewmodel.search.paging.SearchPager
import org.koin.dsl.module

val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(get(), get(),get())
    }

    single<MoviesRepository> {
        MoviesRepositoryImpl(get(), get())
    }

    single<SearchHistoryRepository> {
        LocalRecentSearchRepositoryImpl(get())
    }

}