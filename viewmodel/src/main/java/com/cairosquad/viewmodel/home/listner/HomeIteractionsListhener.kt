package com.cairosquad.viewmodel.home.listner

import com.cairosquad.viewmodel.home.model.MediaType
import com.cairosquad.viewmodel.home.state.HomeScreenState

interface HomeInteractionsListener {
    fun onClickProfile()
    fun onClickTab(tabIndex: Int)
    fun onClickMovie(movieId: Long)
    fun onClickSeries(seriesId: Long)
    fun onClickSeeAllAiringToday()
    fun onClickSeeAllOnTv()
    fun onGenreSelected(genreIndex: Int)
    fun onFilterSelected(filter: HomeScreenState.FilterType)
    fun onClickSeeAllTopRated(mediaType: MediaType)
    fun onClickSeeAllTrending(mediaType: MediaType)
    fun onClickSeeAllFreeToWatch(mediaType: MediaType)
    fun onClickSeeAllUpcoming(mediaType: MediaType)
    fun onClickSeeAllMoreRecommended(mediaType: MediaType)
}