package com.cairosquad.movio.di

import org.koin.dsl.module

val appModule = module {
    includes(
        remoteDataSourceModule,
        domainModule,
        repositoryModule,
        localDataSourceModule,
        viewModelModule
    )
}