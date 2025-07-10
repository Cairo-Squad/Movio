package com.cairosquad.movio.di

import com.cairosquad.domain.search.repository.RecentSearchRepository
import com.cairosquad.domain.search.repository.RecommendationRepository
import com.cairosquad.domain.search.repository.SearchRepository
import com.cairosquad.repository.search.RecentSearchRepositoryImpl
import com.cairosquad.repository.search.RecommendationRepositoryImpl
import com.cairosquad.repository.search.SearchRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(get(), get())
    }

    single<RecommendationRepository> {
        RecommendationRepositoryImpl(get())
    }

    single<RecentSearchRepository> {
        RecentSearchRepositoryImpl(get())
    }

}