package com.cairosquad.movio.di

import com.cairosquad.domain.repository.ArtistsRepository
import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.domain.repository.UserCategoryPreferenceRepository
import com.cairosquad.repository.artists.ArtistsRepositoryImpl
import com.cairosquad.repository.movie.MovieRepositoryImpl
import com.cairosquad.repository.search.SearchRepositoryImpl
import com.cairosquad.repository.search.UserCategoryPreferenceRepositoryImpl
import com.cairosquad.repository.series.SeriesRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(get(), get(), get())
    }

    single<MoviesRepository> {
        MovieRepositoryImpl(get(), get(), get())
    }

    single<SeriesRepository> {
        SeriesRepositoryImpl(get())
    }

    single<ArtistsRepository> {
        ArtistsRepositoryImpl(get(), get())
    }

    single<UserCategoryPreferenceRepository> {
        UserCategoryPreferenceRepositoryImpl(get())
    }

}