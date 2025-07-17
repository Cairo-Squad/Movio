package com.cairosquad.movio.di

import com.cairosquad.domain.repository.ArtistsRepository
import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.repository.artists.ArtistsRepositoryImpl
import com.cairosquad.repository.movies.MoviesRepositoryImpl
import com.cairosquad.repository.series.SeriesRepositoryImpl
import com.cairosquad.repository.search.SearchRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(get(), get(),get())
    }

    single<MoviesRepository> {
        MoviesRepositoryImpl(get(), get())
    }

    single<SeriesRepository> {
        SeriesRepositoryImpl()
    }

    single<ArtistsRepository> {
        ArtistsRepositoryImpl()
    }

}