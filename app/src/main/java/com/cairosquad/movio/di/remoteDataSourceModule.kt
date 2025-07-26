package com.cairosquad.movio.di

import com.cairosquad.remote.artists.RemoteArtistDataSourceImpl
import com.cairosquad.remote.login.RemoteLoginDataSourceImpl
import com.cairosquad.remote.movie.RemoteMovieDataSourceImpl
import com.cairosquad.remote.search.RemoteMovieDiscoveryDataSourceImpl
import com.cairosquad.remote.search.RemoteSearchDataSourceImpl
import com.cairosquad.remote.series.RemoteSeriesDataSourceImpl
import com.cairosquad.remote.utils.retrofit.ApiServiceProvider
import com.cairosquad.repository.artists.data_source.ArtistsRemoteDataSource
import com.cairosquad.repository.login.data_source.remote.RemoteLoginDataSource
import com.cairosquad.repository.movie.data_source.remote.RemoteMovieDataSource
import com.cairosquad.repository.search.data_source.remote.RemoteMovieDiscoveryDataSource
import com.cairosquad.repository.search.data_source.remote.RemoteSearchDataSource
import com.cairosquad.repository.series.data_source.remote.RemoteSeriesDataSource
import org.koin.dsl.module

val remoteDataSourceModule = module {

    single {
        ApiServiceProvider()
    }

    single<RemoteLoginDataSource> {
        RemoteLoginDataSourceImpl(get())
    }

    single<RemoteSearchDataSource> {
        RemoteSearchDataSourceImpl(get())
    }

    single<RemoteMovieDiscoveryDataSource> {
        RemoteMovieDiscoveryDataSourceImpl(get())
    }

    single<ArtistsRemoteDataSource> {
        RemoteArtistDataSourceImpl(get())
    }

    single<RemoteMovieDataSource> {
        RemoteMovieDataSourceImpl(get())
    }

    single<RemoteSeriesDataSource> {
        RemoteSeriesDataSourceImpl(get())
    }

}