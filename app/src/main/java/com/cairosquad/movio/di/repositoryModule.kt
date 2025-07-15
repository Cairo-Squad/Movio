package com.cairosquad.movio.di

import com.cairosquad.domain.search.repository.SearchHistoryRepository
import com.cairosquad.domain.search.repository.DiscoveryRepository
import com.cairosquad.domain.search.repository.SearchRepository
import com.cairosquad.repository.search.LocalRecentSearchRepositoryImpl
import com.cairosquad.repository.search.DiscoveryRepositoryImpl
import com.cairosquad.repository.search.SearchRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(get(), get())
    }

    single<DiscoveryRepository> {
        DiscoveryRepositoryImpl(get(), get())
    }

    single<SearchHistoryRepository> {
        LocalRecentSearchRepositoryImpl(get())
    }

}