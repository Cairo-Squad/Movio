package com.cairosquad.movio.di

import com.cairosquad.domain.repository.AccountRepository
import com.cairosquad.domain.repository.ArtistsRepository
import com.cairosquad.domain.repository.LoginRepository
import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.domain.repository.SearchRecommendationRepository
import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.repository.account.AccountRepositoryImpl
import com.cairosquad.repository.artists.ArtistsRepositoryImpl
import com.cairosquad.repository.login.LoginRepositoryImpl
import com.cairosquad.repository.movie.MovieRepositoryImpl
import com.cairosquad.repository.search.SearchRecommendationRepositoryImpl
import com.cairosquad.repository.search.SearchRepositoryImpl
import com.cairosquad.repository.series.SeriesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLoginRepository(
        impl: LoginRepositoryImpl
    ): LoginRepository

    @Binds
    @Singleton
    abstract fun bindSearchRepository(
        impl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    @Singleton
    abstract fun bindSearchRecommendationRepository(
        impl: SearchRecommendationRepositoryImpl
    ): SearchRecommendationRepository

    @Binds
    @Singleton
    abstract fun bindMoviesRepository(
        impl: MovieRepositoryImpl
    ): MoviesRepository

    @Binds
    @Singleton
    abstract fun bindSeriesRepository(
        impl: SeriesRepositoryImpl
    ): SeriesRepository

    @Binds
    @Singleton
    abstract fun bindArtistsRepository(
        impl: ArtistsRepositoryImpl
    ): ArtistsRepository

    @Binds
    @Singleton
    abstract fun bindAccountRepository(
        impl: AccountRepositoryImpl
    ): AccountRepository
}
