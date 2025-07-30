package com.cairosquad.movio.di

import com.cairosquad.domain.usecase.LoginUseCase
import com.cairosquad.domain.usecase.ManageArtistUseCase
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.domain.usecase.ManageSearchHistoryUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::ManageSearchHistoryUseCase)
    singleOf(::ManageArtistUseCase)
    singleOf(::LoginUseCase)
    singleOf(::ManageMoviesUseCase)
    singleOf(::ManageSeriesUseCase)
}