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
import org.koin.dsl.module
import retrofit2.Retrofit

val remoteDataSourceModule = module {

    single {
        retrofitProvider()
    }

    single<LoginApiService> {
        get<Retrofit>().create(LoginApiService::class.java)
    }

    single<SeriesApiService> {
        get<Retrofit>().create(SeriesApiService::class.java)
    }

    single<RemoteLoginDataSource> {
        RemoteLoginDataSourceImpl(get())
    }

    single<ArtistsRemoteDataSource> {
        RemoteArtistDataSourceImpl(get())
    }

    single<MoviesRemoteDataSource> {
        MoviesRemoteDataSourceImpl(get())
    }

    single<SeriesRemoteDataSource> {
        SeriesRemoteDataSourceImpl(get())
    }

    single<ArtistsApiService> {
        get<Retrofit>().create(ArtistsApiService::class.java)
    }
    single<MovieApiService> {
        get<Retrofit>().create(MovieApiService::class.java)
    }
}