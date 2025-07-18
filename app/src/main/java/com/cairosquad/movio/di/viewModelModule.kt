package com.cairosquad.movio.di

import com.cairosquad.viewmodel.details.similar_movies.SimilarMoviesViewModel
import com.cairosquad.viewmodel.search.SearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    viewModelOf(::SimilarMoviesViewModel)
}