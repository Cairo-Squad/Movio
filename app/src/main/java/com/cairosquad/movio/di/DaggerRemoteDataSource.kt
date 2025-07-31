package com.cairosquad.movio.di

import com.cairosquad.remote.artists.ArtistsApiService
import com.cairosquad.remote.artists.RemoteArtistDataSourceImpl
import com.cairosquad.remote.login.LoginApiService
import com.cairosquad.remote.login.RemoteLoginDataSourceImpl
import com.cairosquad.remote.movie.MovieApiService
import com.cairosquad.remote.movie.MoviesRemoteDataSourceImpl
import com.cairosquad.remote.series.SeriesApiService
import com.cairosquad.remote.series.SeriesRemoteDataSourceImpl
import com.cairosquad.remote.utils.retrofit.retrofitProvider
import com.cairosquad.repository.artists.data_source.remote.ArtistsRemoteDataSource
import com.cairosquad.repository.login.data_source.remote.RemoteLoginDataSource
import com.cairosquad.repository.movie.data_source.remote.MoviesRemoteDataSource
import com.cairosquad.repository.series.data_source.remote.SeriesRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    // Retrofit provider
    companion object {
        @Provides
        @Singleton
        fun provideRetrofit(): Retrofit = retrofitProvider()

        @Provides
        @Singleton
        fun provideLoginApiService(retrofit: Retrofit): LoginApiService =
            retrofit.create(LoginApiService::class.java)

        @Provides
        @Singleton
        fun provideSeriesApiService(retrofit: Retrofit): SeriesApiService =
            retrofit.create(SeriesApiService::class.java)

        @Provides
        @Singleton
        fun provideArtistsApiService(retrofit: Retrofit): ArtistsApiService =
            retrofit.create(ArtistsApiService::class.java)

        @Provides
        @Singleton
        fun provideMovieApiService(retrofit: Retrofit): MovieApiService =
            retrofit.create(MovieApiService::class.java)
    }

    @Binds
    @Singleton
    abstract fun bindRemoteLoginDataSource(
        impl: RemoteLoginDataSourceImpl
    ): RemoteLoginDataSource

    @Binds
    @Singleton
    abstract fun bindArtistsRemoteDataSource(
        impl: RemoteArtistDataSourceImpl
    ): ArtistsRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindMoviesRemoteDataSource(
        impl: MoviesRemoteDataSourceImpl
    ): MoviesRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindSeriesRemoteDataSource(
        impl: SeriesRemoteDataSourceImpl
    ): SeriesRemoteDataSource
}
