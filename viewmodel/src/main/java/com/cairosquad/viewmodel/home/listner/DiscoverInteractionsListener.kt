package com.cairosquad.viewmodel.home.listner

import com.cairosquad.viewmodel.home.state.DiscoverScreenState
import com.cairosquad.viewmodel.home.model.MediaType

interface DiscoverInteractionsListener {
    fun onGenreSelected(genreIndex: Int)
    fun onFilterSelected(filter: DiscoverScreenState.FilterType)
    fun onClickMovie(movieId: Long)
    fun onClickSeries(seriesId: Long)
    fun onClickSeeAllTopRated(mediaType: MediaType)
    fun onClickSeeAllTrending(mediaType: MediaType)
    fun onClickSeeAllFreeToWatch(mediaType: MediaType)
    fun onClickSeeAllUpcoming(mediaType: MediaType)
    fun onClickSeeAllMoreRecommended(mediaType: MediaType)

}