package com.cairosquad.movio.di

import com.cairosquad.remote.search.RemoteMovieDiscoveryDataSourceImpl
import com.cairosquad.remote.search.RemoteSearchDataSourceImpl
import com.cairosquad.remote.utils.HttpClientFactory
import com.cairosquad.remote.utils.HttpEngine
import com.cairosquad.repository.search.data_source.remote.RemoteMovieDiscoveryDataSource
import com.cairosquad.repository.search.data_source.remote.RemoteSearchDataSource
import org.koin.dsl.module

val remoteDataSourceModule = module {
    single {
        HttpClientFactory.create(
            engine = HttpEngine.provide()
        )
    }

    single<RemoteSearchDataSource> {
        RemoteSearchDataSourceImpl(get())
    }

    single<RemoteMovieDiscoveryDataSource> {
        RemoteMovieDiscoveryDataSourceImpl(get())
    }
}