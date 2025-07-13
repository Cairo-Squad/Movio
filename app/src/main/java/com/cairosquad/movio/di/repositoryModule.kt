package com.cairosquad.movio.di

import com.cairosquad.domain.search.repository.SearchHistoryRepository
import com.cairosquad.domain.search.repository.MovieDiscoveryRepository
import com.cairosquad.domain.search.repository.SearchRepository
import com.cairosquad.repository.search.LocalRecentSearchRepositoryImpl
import com.cairosquad.repository.search.RemoteMovieDiscoveryRepositoryImpl
import com.cairosquad.repository.search.SearchRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(get(), get())
    }

    single<MovieDiscoveryRepository> {
        RemoteMovieDiscoveryRepositoryImpl(get())
    }

    single<SearchHistoryRepository> {
        LocalRecentSearchRepositoryImpl(get())
    }

}