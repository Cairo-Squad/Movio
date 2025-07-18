package com.cairosquad.movio.di

import com.cairosquad.viewmodel.foryou.ForYouViewModel
import com.cairosquad.viewmodel.search.SearchViewModel
import com.cairosquad.viewmodel.search.paging.SearchPager
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    viewModelOf(::ForYouViewModel)
}