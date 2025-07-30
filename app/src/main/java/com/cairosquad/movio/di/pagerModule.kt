package com.cairosquad.movio.di

import com.cairosquad.viewmodel.foryou.ForYouPager
import com.cairosquad.viewmodel.search.SearchViewModel
import com.cairosquad.viewmodel.search.paging.SearchPager
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val pagerModule = module {
    viewModelOf(::SearchViewModel)
    single { SearchPager(get(), get(), get()) }
    single { ForYouPager(get()) }
}