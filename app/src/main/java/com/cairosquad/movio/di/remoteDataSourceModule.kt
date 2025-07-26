package com.cairosquad.movio.di

import com.cairosquad.remote.artists.ArtistsApiService
import com.cairosquad.remote.artists.RemoteArtistDataSourceImpl
import com.cairosquad.remote.login.LoginApiService
import com.cairosquad.remote.login.RemoteLoginDataSourceImpl
import com.cairosquad.remote.movie.MovieApiService
import com.cairosquad.remote.movie.MoviesRemoteDataSourceImpl
import com.cairosquad.remote.search.RemoteSearchDataSourceImpl
import com.cairosquad.remote.search.SearchApiService
import com.cairosquad.remote.series.SeriesApiService
import com.cairosquad.remote.series.SeriesRemoteDataSourceImpl
import com.cairosquad.remote.utils.retrofit.retrofitProvider
import com.cairosquad.repository.artists.data_source.ArtistsRemoteDataSource
import com.cairosquad.repository.login.data_source.remote.RemoteLoginDataSource
import com.cairosquad.repository.movie.data_source.remote.MoviesRemoteDataSource
import com.cairosquad.repository.search.data_source.remote.RemoteSearchDataSource
import com.cairosquad.repository.series.data_source.remote.SeriesRemoteDataSource
import com.cairosquad.repository.utils.authenticationTokenProvider
import org.koin.dsl.module
import retrofit2.Retrofit

val remoteDataSourceModule = module {

    single {
        retrofitProvider (
            tokenProvider = { authenticationTokenProvider(get()) }
        )
    }

    single<LoginApiService> {
        get<Retrofit>().create(LoginApiService::class.java)
    }

    single<SeriesApiService> {
        get<Retrofit>().create(SeriesApiService::class.java)
    }

    single<SearchApiService> {
        get<Retrofit>().create(SearchApiService::class.java)
    }

    single<RemoteLoginDataSource> {
        RemoteLoginDataSourceImpl(get())
    }

    single<RemoteSearchDataSource> {
        RemoteSearchDataSourceImpl(get())
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