package com.cairosquad.viewmodel.home.listner

import com.cairosquad.viewmodel.home.state.DiscoverScreenState

interface DiscoverInteractionsListener {
    fun onGenreSelected(genreIndex: Int)
    fun onFilterSelected(filter: DiscoverScreenState.FilterType)
    fun onClickMovie(movieId: Long)
    fun onClickSeries(seriesId: Long)
}