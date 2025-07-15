package com.cairosquad.movio.di

import com.cairosquad.domain.search.repository.ArtistRepository
import com.cairosquad.domain.search.repository.SearchHistoryRepository
import com.cairosquad.domain.search.repository.MovieDiscoveryRepository
import com.cairosquad.domain.search.repository.MoviesRepository
import com.cairosquad.domain.search.repository.SearchRepository
import com.cairosquad.domain.search.repository.SeriesRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Series
import com.cairosquad.repository.details.ArtistRepositoryImpl
import com.cairosquad.repository.details.MoviesRepositoryImpl
import com.cairosquad.repository.details.SeriesRepositoryImpl
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

    single<MoviesRepository> {
        MoviesRepositoryImpl()
    }

    single<SeriesRepository> {
        SeriesRepositoryImpl()
    }

    single<ArtistRepository> {
        ArtistRepositoryImpl()
    }

}