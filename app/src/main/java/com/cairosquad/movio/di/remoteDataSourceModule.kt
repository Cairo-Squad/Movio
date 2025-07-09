package com.cairosquad.movio.di

import com.cairosquad.remote.common.HttpClientFactory
import com.cairosquad.remote.common.HttpEngine
import com.cairosquad.remote.search.RemoteSearchDataSourceImpl
import com.cairosquad.repository.search.dataSource.remote.search.RemoteSearchDataSource
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
}