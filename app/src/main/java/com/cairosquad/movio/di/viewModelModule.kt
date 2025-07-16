package com.cairosquad.movio.di

import com.cairosquad.viewmodel.search.SearchViewModel
import com.cairosquad.viewmodel.search.paging.ArtistPager
import com.cairosquad.viewmodel.search.paging.MoviePager
import com.cairosquad.viewmodel.search.paging.SeriesPager
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    singleOf(::MoviePager)
    singleOf(::SeriesPager)
    singleOf(::ArtistPager)
}