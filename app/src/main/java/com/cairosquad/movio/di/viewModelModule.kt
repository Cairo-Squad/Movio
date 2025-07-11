package com.cairosquad.movio.di

import com.cairosquad.viewmodel.searchviewmodel.SearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
}