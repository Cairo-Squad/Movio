package com.cairosquad.viewmodel.home

import com.cairosquad.viewmodel.base.BaseViewModel

class HomeViewModel(


)  : BaseViewModel<HomeScreenState,HomeEffect>(HomeScreenState()),
    HomeInteractionsListener{
    override fun onClickProfile() {
        sendEffect(HomeEffect.NavigateToProfile)
    }

    override fun onClickCategory(category: String) {
        sendEffect(HomeEffect.NavigateToCategory(category))
    }

    override fun onClickFeaturedMovie(movieId: Long) {
        sendEffect(HomeEffect.NavigateToFeaturedMovie(movieId))
    }

    override fun onClickMovie(movieId: Long) {
        sendEffect(HomeEffect.NavigateMovie(movieId))
    }

    override fun onClickSeries(seriesId: Long) {
        sendEffect(HomeEffect.NavigateSeries(seriesId))
    }

    override fun onClickSeeAllTopRated() {
        sendEffect(HomeEffect.NavigateToSeeAllTopRated)
    }

    override fun onClickSeeAllTrending() {
        sendEffect(HomeEffect.NavigateToSeeAllTrending)
    }

    override fun onClickSeeAllFreeToWatch() {
        sendEffect(HomeEffect.NavigateToSeeAllFreeToWatch)
    }

    override fun onClickSeeAllUpcoming() {
        sendEffect(HomeEffect.NavigateToSeeAllUpcoming)
    }

    override fun onClickSeeAllMoreRecommended() {
        sendEffect(HomeEffect.NavigateToSeeAllMoreRecommended)
    }

    override fun onClickSeeAllAiringToday() {
        sendEffect(HomeEffect.NavigateToSeeAllAiringToday)
    }

    override fun onClickSeeAllOnTv() {
        sendEffect(HomeEffect.NavigateToSeeAllOnTv)
    }
}